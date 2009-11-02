'''Action handler of get request;'''

from django.db import IntegrityError
from django.db import transaction
from django.db.models import Q
from django.core.exceptions import *

from server.spec.utils import replace_delim_to_space
from server.book.entirety import AuthorEntirety, FileEntirety, BookEntirety
from server.exception import *
from server.book.models import *
from server.logger import main_logger

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


def get_author_queryset(xml):
    '''Makes QuerySet for Author from xml request
    Returns QuerySet'''
    return Q()


def get_file_queryset(xml):
    '''Makes QuerySet for File from xml request
    Returns QuerySet'''
    pass


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



    
