'''Action handler of get request;'''

from string import join

from django.db import IntegrityError
from django.db import transaction
from django.db.models import Q
from django.core.exceptions import *

from server.spec.utils import replace_delim_to_space
from server.spec.exception import *
from server.book.models import *
from server.spec.logger import main_logger



def get_q(object, arg, type=''):
    "Makes Q object for 'object' with search type from 'type' \
    sets 'arg' like argument"
    obj_str = object
    if type:
        obj_str = obj_str + '__' + type
    q = Q()
    q.add((obj_str, arg), 'AND')
    return q

    



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




def make_q_from_tag(node, object, default_search_type='icontains'):
    '''Makes Q objects for tag'''
    id = node.get('id', 0)
    # if tag has attribute 'id'
    if id:
        search_type = ''
        text_search=id
        object = object
    else:
        search_type = node.get('type', default_search_type)
        text_search=node.text.strip()
    return get_q(object, text_search, search_type)

def get_authors_q(node):
    q = q()
    for author_node in node.getchildren():
        q = q & make_q_from_tag(author_node)
    return q

def get_files_q(node):
    q = q()
    for file_node in node.getchildren():
        q = q & make_q_from_tag(author_node)
    return q



def get_q_from_xml(xml):
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
            q = q & make_q_from_tag(node)
                
        # if we found the tag 'lang', add qeuryset to q
        elif node.tag == 'lang':
            q = q & make_q_from_tag(node, default_search_type='')

        # if we found the tag 'annotation', add qeuryset to q
        elif node.tag == 'annotation':
            q = q & make_q_from_tag(node)
            

        # if we found the tag 'authors', add qeuryset to q
        elif node.tag == 'authors':
            q = q & get_authors_q(node)

        # if we found the tag 'files', add qeuryset to q
        elif node.tag == 'files':
            q = q & get_files_q(node)
    



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

                # TODO insert code for last_check and time_found

                elif file_det_node.tag == 'more_info':
                    more_info = file_det_node.text.strip()
                    q = q & Q(book_file__more_info__icontains=more_info)

                # TODO insert code for link_img



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



    
