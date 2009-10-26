'''Action handler'''

import md5

from django.db import IntegrityError
from django.db import transaction
from django.core.exceptions import *

from server.spec.utils import replace_delim_to_space
from server.book.entirety import AuthorEntirety, FileEntirety, BookEntirety
from server.exception import *
from server.book.models import *

ACTION = {
    'get': 1,
    'insert': 2,
}

def get_all_handler(book_entr):
    "Returns all matched book_entiretys"
    return book_entr.get_from_db()


def insert_all_handler(book_entr):
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
            print 'ERROR'
            print 'transaction.rollback()'
            transaction.rollback()
            raise
    else:
        raise InnerServerExcption('All handler got not supported action ' + action)
    print 'transaction.commit()'
    transaction.commit()
    return dict


def xml_exec_insert_unsafe(xml):
    "Execute xml-insert-request usafe"
    if xml.tag != 'book':
        raise InputDataServerException("Not found root tag 'book'")
    
    book = Book(title='', lang='')
    authors = []
    files = []
    annotations = []

    for node in xml.getchildren():
        if node.tag == 'title':
            book.title = replace_delim_to_space(node.text)

        if node.tag == 'lang':
            # TODO add check of correct lang (from LANG_CODE)
            book.lang = replace_delim_to_space(node.text)

        if node.tag == 'authors':
            for author_node in node.getchildren():
                
                # If not 'author' tag in block raise exception
                if author_node.tag != 'author':
                    raise InputDataServerException("Unknown tag '" + author_node.tag +
                                         "' in tag 'authors'")

                for details_node in author_node.getchildren():
                    if details_node.tag == 'name':
                        name = replace_delim_to_space(details_node.text)
                        if not name:
                            raise InputDataServerException("The field 'author.name' can't be empty")
                        
                        author = Author.objects.get_or_create(name=name)[0]
                        print 'Author.objects.get_or_create(name=%s)' % name


                    if details_node.tag == 'alias':
                        name = replace_delim_to_space(details_node.text)
                        if name:
                            alias = Alias.objects.get_or_create(name=name)[0]
                            print 'Alias.objects.get_or_create(name=%s)' % name
                            author.alias.add(alias)

                    authors.append(author)

        if node.tag == 'files':
            for file_node in node.getchildren():
                if file_node.tag != 'file':
                    raise InputDataServerException("Unknown tag '" + file_node.tag +
                                         "' in tag 'files'")

                for details_node in file_node.getchildren():
                    if details_node.tag == 'link':
                        link = replace_delim_to_space(details_node.text)
                        if not link:
                            raise InputDataServerException("The field 'file.link' can't be empty")

                        link_hash = md5.new(link).hexdigest()
                        try:
                            file = BookFile.objects.get(link_hash=link_hash)
                        except ObjectDoesNotExist:
                            file = BookFile(link=link, link_hash=link_hash)
                         
                    if details_node.tag == 'type':
                        file.type = replace_delim_to_space(details_node.text)
                    if details_node.tag == 'size':
                        file.size = replace_delim_to_space(details_node.text)
                    if details_node.tag == 'more_info':
                        file.more_info = details_node.text
                    if details_node.tag == 'img_link':
                        file.img_link = replace_delim_to_space(details_node.text)

                file.save()
                files.append(file)

        if node.tag == 'annotation':
            annotations = Annotation.objects.get_or_create(name=node.text)
        
    found_book = Book.objects.filter(title=book.title, lang=book.lang)
    for author in authors:
        found_book = found_book.filter(author=author)

    print found_book.count()

    if found_book.count() > 1:
        raise InnerServerException("More than one book with the same title and the same authors")
    if not found_book.count():
        if not book.title:
            raise InputDataServerException("The field title can't be empty")
        book.save()
    else:
        book = found_book[0]

    for author in authors:
        book.author_set.add(author)

    for file in files:
        book.book_file.add(file)

    for annotation in annotations:
        book.annotation.add(annotation)

    book.save()

                    
        


@transaction.commit_manually
def xml_exec_insert(xml):
    "Executes xml-request"
    try:
        result = xml_exec_insert_unsafe(xml)
    except:
        print 'transaction.rollback()'
        transaction.rollback()
        raise
    print 'transaction.commit()'
    transaction.commit()
    return result
            
def xml_exec_get(xml):
    "Executes xml-request, returns books"
    if xml.tag != 'book':
        raise InputDataServerException("Not found root tag 'book'")

    books = Book.objects.all()

    for node in xml.getchildren():
        if node.tag == 'title':
            title = replace_delim_to_space(node.text)
            books = books.filter(title__icontains=title)
        elif node.tag == 'lang':
            lang = replace_delim_to_space(node.text)
            print 'lang =', lang
            if lang:
                print 'filter by lang'
                books = books.filter(lang=lang)
                print books

        # Book authors block
        elif node.tag == 'authors':
            for author_node in node.getchildren():
                
                # If not 'author' tag in block raise exception
                if author_node.tag != 'author':
                    raise InputDataServerException("Unknown tag '" + author_node.tag +
                                         "' in tag 'authors'")

                for details_node in author_node.getchildren():
                    if details_node.tag == 'name':
                        name = replace_delim_to_space(details_node.text)
                        if name:
                            books = books.filter(author__name__icontains=name)
                    if details_node.tag == 'alias':
                        alias = replace_delim_to_space(details_node.text)
                        if alias:
                            books = books.filter(author__alias__name__icontains=alias)

        # Book file block
        elif node.tag == 'files':
            for file_node in node.getchildren():
                
                # If not 'file' tag in block raise exception
                if file_node.tag != 'file':
                    raise InputDataServerException("Unknown tag '" + file_node.tag +
                                         "' in tag 'files'")

                for details_node in file_node.getchildren():
                    if details_node.tag == 'link':
                        link = replace_delim_to_space(details_node.text)
                        if link:
                            books = books.filter(book_file__link=link)
                    if details_node.tag == 'size':
                        size = replace_delim_to_space(details_node.text)
                        try:
                            if size: books = books.filter(book_file__size=size)
                        except ValueError:
                            raise InputDataServerException("size is not number")
                    if details_node.tag == 'type':
                        type = replace_delim_to_space(details_node.text)
                        if type:
                            books = books.filter(book_file__type=type)
                    if details_node.tag == 'more_info':
                        more_info = replace_delim_to_space(details_node.text)
                        if more_info:
                            books = books.filter(book_file__more_info__icontains=more_info)
                    if details_node.tag == 'img_link':
                        img_link = replace_delim_to_space(details_node.text)
                        if img_link:
                            books = books.filter(book_file__img_link=img_link)

    return books
            
