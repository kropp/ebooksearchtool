
try:
    from hashlib import md5
except ImportError:
    from md5 import new as md5

from django.db import IntegrityError
#from django.core.exceptions import ObjectDoesNotExist

from spec.exception import InputDataServerException

from book.models import Author, Book, BookFile, Language

def get_tag_text(xml, tag):
    "Finds 'tag' in 'xml', returns text content or None."
    node = xml.find(tag)
    if node is not None:
        text = node.text.strip()
        if not text:
            return None
        return text

def get_id(xml):
    """
    Tries to get 'id', if atribute doesn't exist, return None,
    else return 'id'
    """
    try:
        return int(xml.attrib['id'])
    except ValueError:
        raise InputDataServerException("'id' has to be integer.")
    except KeyError:
        return None


def update_author(xml):
    "Executes xml, creates author or updates inf about it."
    entity_id = get_id(xml)

    if entity_id:
        # Modify existing author
        author = Author.objects.get(id=entity_id)
    else:
        # Create new author
        author = Author()

    full_name = get_tag_text(xml, 'full_name')
    if full_name:
        author.name = full_name
    else:
        raise IntegrityError("'full_name' can't be empty")

    try:
        credit_str = get_tag_text(xml, 'credit')
        if credit_str:
            credit = int(credit_str)
            author.credit = credit
    except ValueError, ex:
        raise InputDataServerException(ex)

    author.save()
    return author
                

def update_book_file(xml):
    "Executes xml, creates book_file or updates inf about it."
    entity_id = get_id(xml)

    if entity_id:
        # Modify existing book_file
        book_file = BookFile.objects.get(id=entity_id)
    else:
        # Create new book_file
        book_file = BookFile()

    link = get_tag_text(xml, 'link')
    file_type = get_tag_text(xml, 'type')
    more_info = get_tag_text(xml, 'more_info')
    img_link = get_tag_text(xml, 'img_link')
    try:
        size = int(get_tag_text(xml, 'size'))
        credit = int(get_tag_text(xml, 'credit'))
    except ValueError, ex:
        raise InputDataServerException(ex)

    if link:
        book_file.link = link
    if file_type:
        book_file.type = file_type
    if more_info:
        book_file.more_info = more_info
    if img_link:
        book_file.img_link = img_link
    if size:
        book_file.size = size
    if credit:
        book_file.credit = credit

    book_file.save()


def update_book(xml):
    "Executes xml, creates book or updates inf about it."
    entity_id = get_id(xml)

    if entity_id:
        # Modify existing book
        book = Book.objects.get(id=entity_id)
    else:
        # Create new book
        book = Book()

    title = get_tag_text(xml, 'title')
    lang = get_tag_text(xml, 'lang')

    # TODO get language
    lang = Language.objects.get(short='?')

    try:
        credit = int(get_tag_text(xml, 'credit'))
    except ValueError, ex:
        raise InputDataServerException(ex)
    if title:
        book.title = title
    if lang:
        book.language = lang
    if credit:
        book.credit = credit

    book.save()

    # authors tag
    authors = xml.find('authors')
    if authors:
        # Get id of all authors
        authors_id =  \
            [int(author.attrib['id']) for author in authors.findall('author')]

        if xml.attrib.get('reset') == 'author':
            old_authors_id = set(book.author_set.values_list('id', flat=True))
            rm_authors_id = old_authors_id.difference(authors_id)
            rm_authors = Author.objects.filer(id__in=rm_authors_id)

            book.author_set.remove(rm_authors)
            # TODO check rm_authors for hide in result, if its don't have book
        
        for author_id in authors_id:
            book.author_set.add(author_id)

    # TODO add for book_file

