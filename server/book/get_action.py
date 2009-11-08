'''Action handler of get request;'''

from django.db import IntegrityError
from django.db import transaction
from django.db.models import Q
from django.core.exceptions import *

from server.spec.utils import replace_delim_to_space
from server.spec.exception import *
from server.book.models import *
from server.spec.logger import main_logger

def get_q(object, type, arg):
    "Makes Q object for 'object' with search type from 'type' \
    sets 'arg' like argument"

    if object == 'title':
        if type == 'icontains':
            return Q(title__icontains=arg)
    elif object == 'author__name':
        if type == 'icontains':
            return Q(author__name__icontains=arg)

    elif object == 'file__timefound':
        if type == '=':
            return Q(file__timefound=arg)
        elif type == '>':
            return Q(file__timefound__gt=arg)
        elif type == '<':
            return Q(file__timefound__lt=arg)
        
    
    #        raise InputDataServerException("Unknow type for object 'title'")


    



def check_request(requst_type, xml):
    "Checks xml request"
    pass    


def get_by_id(entirety, node):
    "Trys to get entirety by id in attribute; Returns None if id is not defined"
    try:
        # gets id from attribute
        id = node.attrib['id']
        return entirety.objects.get(id=id)
    except ValueError:
        raise InputDataServerException("The %s id must be int" % (entirety.__name__))
    except ObjectDoesNotExist:
        raise InputDataServerException("The %s with id = %s does not exist in database" % (entirety.__name__, id)) 
    except KeyError:
        pass
    return None


def make_q_from_tag(node, default_search_type='icontains'):
    '''Makes Q objects for tag'''

    # Try get Q by id
    q = get_by_id(node.tag, node)
    if q:
        return q

    search_type = node.get('type', default_search_type)

    return get_q(node.tag, search_type, node.text.strip())






def get_author_queryset(xml):
    '''Makes Q object for Author from xml request
    Returns Q object'''

    q = Q()

    # for each author
    for node in xml.getchildren():
        author = get_by_id(Author, node)
        if author:
            q = q & Q(author__id=author.id)
        else:
            name = replace_delim_to_space(node.text)
            q = q & Q(author__name__icontains=name)

    return q


def get_file_queryset(xml):
    '''Makes QuerySet for File from xml request
    Returns QuerySet'''

    q = Q()

    # for each file
    for node in xml.getchildren():
        file = get_by_id(BookFile, node)
        if file:
            q = q & Q(book_file__id=file.id)
        else:
            for file_det_node in node.getchildren():
                if file_det_node.tag == 'link':
                    link = replace_delim_to_space(file_det_node.text)
                    q = q & Q(book_file__link=link)
                elif file_det_node.tag == 'size':
                    size = replace_delim_to_space(file_det_node.text)
                    q = q & Q(book_file__size=size)
                elif file_det_node.tag == 'type':
                    type = replace_delim_to_space(file_det_node.text)
                    q = q & Q(book_file__type=type)
                    


def get_book_queryset(xml):
    '''Makes QuerySet for Book from xml request
    Returns QuerySet'''

    # if they are book id, return it
    book = get_by_id(Book, xml)
    if book:
        return [book]

    q = Q()
    
    # for by all tags in <book>
    for node in xml.getchildren():
        # if we found the tag 'title', add qeuryset to q
        if node.tag == 'title':
            title = replace_delim_to_space(node.text)
            q = q & Q(title__icontains=title)
                
        # if we found the tag 'tag', add qeuryset to q
        elif node.tag == 'lang':
            lang = replace_delim_to_space(node.text)
            q = q & Q(lang=lang)

        # if we found the tag 'annotation', add qeuryset to q
        elif node.tag == 'annotation':
            annotation = get_by_id(Annotation, node)
            if annotation:
                q = q & Q(annotation=annotation)
            else:
                q = q & Q(annotation__name__icontains=node.text)


        # if we found the tag 'authors', add qeuryset to q
        elif node.tag == 'authors':
            q = q & get_author_queryset(node)

        # if we found the tag 'files', add qeuryset to q
        elif node.tag == 'files':
            q = q & get_file_queryset(node)



    
