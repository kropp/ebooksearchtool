
try:
    from hashlib import md5
except ImportError:
    from md5 import new as md5

from django.db import IntegrityError

from book.models import Author, Book, BookFile, Language
from spec.exception import InputDataServerException

def get_tag_text(xml, tag):
    "Finds 'tag' in 'xml', returns text content or None."
    node = xml.find(tag)
    if node:
        return node.text


def create_book_file(xml):
    "Gets data from xml, creates book_file, returns created book_file."
    # Get data from xml
    link = get_tag_text(xml, 'link')
    link_hash = md5(link).hexdigest()
    type = get_tag_text(xml, 'type')
    more_info = get_tag_text(xml, 'more_info')
    img_link = get_tag_text(xml, 'img_link')

    try:
        size = int(get_tag_text(xml, 'size'))
        credit = int(get_tag_text(xml, 'credit'))
    except ValueError, ex:
        raise InputDataServerException(ex)

    # Create book_file
    book_file = BookFile(link=link, link_hash=link_hash, type=type, \
                         size=size, more_info=more_info, \
                         img_link=img_link, credit=credit)
    book_file.save()
    return book_file


def create_book(xml):
    "Gets data from xml, creates book, returns created book."
    # Get data from xml
    title = get_tag_text(xml, 'title')
    # TODO add language checker here
    lang = Language.objects.get(short='?')

    try:
        credit = int(get_tag_text(xml, 'credit'))
    except ValueError, ex:
        raise InputDataServerException(ex)

    # Create book
    book = Book(title=title, language=lang, credit=credit)
    book.save()
    return book


def create_author(xml):
    "Gets data from xml, creates author, returns created author."
    # Get data from xml
    name = get_tag_text(xml, 'name')

    try:
        credit = int(get_tag_text(xml, 'credit'))
    except ValueError, ex:
        raise InputDataServerException(ex)

    # Create author
    author = Author(name=name, credit=credit)
    author.save()
    return author



def define_entity(xml):
    "Creates entity, returns dictionary with mapping from 'ui' to 'id'."

    # Mapping from 'ui' to 'id'
    ui_dic = {}

    try:
        for node in xml.getchildren():
    
            # Get 'ui' from tag attribute
            entity_ui = int(node.attrib['ui'])
            # Check 'ui' uniqueness
            if entity_ui in ui_dic:
                raise InputDataServerException("For '%s' ui=%s is not unique" %
                                               (node.tag, entity_ui))
        
            # Create entity
            if node.tag == 'author':
                entity_id = create_author(node).id
            elif node.tag == 'file':
                entity_id = create_book_file(node).id
            elif node.tag == 'book':
                entity_id = create_book(node).id
            else:
                raise InputDataServerException(
                        "Unexpected tag '%s' in define section" % (node.tag,))
            ui_dic[entity_ui] = entity_id
    
    except IntegrityError, ex:
        raise InputDataServerException(ex)
    except KeyError:
        raise InputDataServerException("One entity dosn't have 'ui' attrubute")
    except ValueError:
        raise InputDataServerException("'ui' has to be integer.")

    return ui_dic


def update_entity(xml, ui_dic):

    # Replace all 'ui' to 'id'
    try:
        for node in xml.getiterator():
            if 'ui' in node.attrib:
                node.set('id', ui_dic[int(node.attrib['ui'])] )
    except ValueError:
        raise InputDataServerException("'ui' have to be integer.")
    except KeyError:
        raise InputDataServerException("Using undefined 'ui'.")

    for node in xml.getchildren():
        # Check 'id'
        if not node.attrib.get('id'):
            raise InputDataServerException(
                    "'%s' in update section doesn't have 'id' or 'ui'." %
                    (node.tag,))
        if node.tag == 'author':
            entity_id = create_author(node).id
        elif node.tag == 'file':
            entity_id = create_book_file(node)
        elif node.tag == 'book':
            entity_id = create_book(node)
        else:
            raise InputDataServerException(
                    "Unexpected tag '%s' in define section" % (node.tag,))


@transaction.commit_manually
def exec_update(xml):
    
    define_xml = xml.find('define')
    update_xml = xml.find('update')

    if not define_xml:
        raise InputDataServerException("Not found tag 'define'")
    if not update_xml:
        raise InputDataServerException("Not found tag 'update'")
        
    try:
        # Create entities from define section
        ui_dict = define_entity(define_xml)
        # Update entities
        update_entity(update_xml, ui_dict)
    except:
        transaction.rollback()
        raise

    transaction.commit()

