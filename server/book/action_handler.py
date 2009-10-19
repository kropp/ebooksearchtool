'''Action handler'''

from django.db import IntegrityError
from django.db import transaction

from server.spec.utils import replace_delim_to_space
from server.book.entirety import AuthorEntirety, FileEntirety, BookEntirety
from server.exception import *

ACTION = {
    'get': 1,
    'insert': 2,
}

def get_all_handler(book_entr):
    "Get book"
    book_entrs = book_entr.get_from_db()
    for book_entr in book_entrs:
        print book_entr
    return book_entrs
    
  #  a = AuthorEntirety()
    #print a.get_from_db()
  #  print BookEntirety('').get_from_db()
  #  book = BookEntirety('')
  #  book_obj_list = book.get_from_db()
  #  AuthorEntirety.CreateFromObj('')


def insert_all_handler(data_dict):
    print data_dict['author']
    a = AuthorEntirety('author55', ['author4alias12', 'author4alias22'])
    f = FileEntirety('http://link', 123, 'epub') # FILE_TYPE['epub'])

    b = BookEntirety('Title', [a], [f])
    dict = {'id': b.save_to_db(), }
    return dict


@transaction.commit_manually
def all_handler(action, book_entr):
    "Handler of all requests, maintains the integrity of database"
    if action == ACTION['get']:
        return get_all_handler(book_entr)
    elif action == ACTION['insert']:
        try:
            dict = insert_all_handler(book_entr)
        except Exception:
            transaction.rollback()
            raise
    else:
        raise DataExcpt(40001)
    transaction.commit()
    return dict




    

def load_book_entr_from_xml(xml):
    "Creates BookEntirety and fills it data from xml document"
    if xml.tag != 'book':
        raise InputDataExcpt("Not found root tag 'book'")

    book_entr = BookEntirety(title='')


    for node in xml.getchildren():
        if node.tag == 'title':
            book_entr.title = replace_delim_to_space(node.text)
        elif node.tag == 'lang':
            lang = replace_delim_to_space(node.text)
            book_entr.lang = lang

        # Book authors block
        elif node.tag == 'authors':
            for author_node in node.getchildren():
                
                # If not 'author' tag in block raise exception
                if author_node.tag != 'author':
                    raise InputDataExcpt("Unknown tag '" + author_node.tag +
                                         "' in tag 'authors'")

                # Create author entirety and fill data
                for details_node in author_node.getchildren():
                    author_entr = AuthorEntirety()                    
                    if details_node.tag == 'name':
                        author_entr.name = replace_delim_to_space(details_node.text)
                    if details_node.tag == 'alias':
                        author_entr.aliases.append(replace_delim_to_space(details_node.text))
                    # Add created author to authors list of book entirety
                    book_entr.authors.append(author_entr)

        # Book file block
        elif node.tag == 'files':
            for file_node in node.getchildren():
                
                # If not 'file' tag in block raise exception
                if file_node.tag != 'file':
                    raise InputDataExcpt("Unknown tag '" + file_node.tag +
                                         "' in tag 'files'")

                # Create file entirety and fill data
                for details_node in file_node.getchildren():
                    file_entr = FileEntirety(link='')                    
                    if details_node.tag == 'link':
                        file_entr.link = replace_delim_to_space(details_node.text)
                    if details_node.tag == 'size':
                        file_entr.size = replace_delim_to_space(details_node.text)
                    if details_node.tag == 'type':
                        file_entr.type = replace_delim_to_space(details_node.text)
                    if details_node.tag == 'more_info':
                        file_entr.more_info = replace_delim_to_space(details_node.text)
                    if details_node.tag == 'img_link':
                        file_entr.img_link = replace_delim_to_space(details_node.text)

                    # Add created book file to book_files list of book entirety
                    book_entr.files.append(file_entr)

    return book_entr
            
