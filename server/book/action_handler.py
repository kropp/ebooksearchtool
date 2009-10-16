'''Action handler'''


import server.book.models as book
from server.exception import *
from server.book.fileTypes import FILE_TYPE

from django.db import IntegrityError
from django.db import transaction




ACTION = {
    'get': 1,
    'insert': 2,
    'update': 3,
    'remove': 4,}

class AuthorEntirety:
    def __init__(self, name='', aliases=[]):
        self.name = name
        self.aliases = aliases
    
    def save_to_db(self):
        "Saves entirety author to database. Ruturns saved author"
        if not self.name:
            raise InputDataExcpt(21101)

        author = book.Author.objects.get_or_create(name=self.name)[0]

        # get all aliases
        for alias_item in self.aliases:
            # get or create alias in database
            alias_obj = book.Alias.objects.get_or_create(name=alias_item)[0]
            alias_id = None
            if alias_obj.author_set.count() > 0:
                alias_id = alias_obj.author_set.get().id
            # if existing alias associated with other author -> exception
            if alias_id and alias_id != author.id:
                raise InputDataExcpt(32102)
            # add alias to author
            alias_obj.author_set.add(author)
            alias_obj.save()

        return author
       
    def get_from_db(self):
        "Return list of authors"
        
        if self.aliases:
            "Get authors by name and alias"
            author_list = []
            for alias_name in aliases:
                author_list += book.Author.objects.filter(name__icontains=self.name, alias__name__icontains=alias__name__icontains)
            # remove duplicates
            author_dict = {}
            for author in author_list:
                author_dict[author.name] = author
            author_list = author_dict
        else:
            "Get authors only by name"
            author_list = book.Author.objects.filter(name__icontains=self.name)
        return author_list
        



class FileEntirety:
    def __init__(self, link, size, type=None,
                 more_info='', img_link=''):
        self.link = link
        self.size = size
        self.type = type
        self.more_info = more_info
        self.img_link = img_link

    def save_to_db(self):
        book_file = book.BookFile.objects.get_or_create(link=self.link, size=self.size)[0]
        book_file.size = self.size
        if self.type:
            book_file.type = self.type
        if self.more_info:
            book_file.more_info = self.more_info
        if self.img_link:
            book_file.img_link = self.img_link

        book_file.save()

        return book_file




class BookEntirety:
    def __init__(self, title, authors=[], files=[], annotations=[]):
        self.title = title
        self.authors = authors
        self.files = files
        self.annotations = annotations

    def save_to_db(self):
        book_obj = book.Book.objects.get_or_create(title=self.title)[0]

        for author in self.authors:
            author_obj = author.save_to_db()
            author_obj.book.add(book_obj)

        for file in self.files:
            file_obj = file.save_to_db()
            book_obj.book_file.add(file_obj)


        


    def get_from_db(self):
        "returns list of matched BookEntirety"
        book_list = book.Book.objects.filter(title__icontains=self.title)
        return book_list




def get_all_handler(data_dict):
    "Get book"
    a = AuthorEntirety()
    print a.get_from_db()


def insert_all_handler(data_dict):
    print data_dict['author']
    a = AuthorEntirety('author55', ['author4alias12', 'author4alias22'])
    f = FileEntirety('http://link', 123, 'epub') # FILE_TYPE['epub'])

    b = BookEntirety('Title', [a], [f])
    b.save_to_db()



@transaction.commit_manually
def all_handler(action, data_dict):
    "Handler of all requests, maintains the integrity of database"
    if action == ACTION['get']:
        return get_all_handler(data_dict)
    elif action == ACTION['insert']:
        try:
            dict = insert_all_handler(data_dict)
        except Exception:
            transaction.rollback()
            raise
    else:
        raise DataExcpt(40001)
    transaction.commit()
    return dict






def author_handler(action, data_dict):
    #print data_dict
    if action == ACTION['insert']:
        try:
            author_name = data_dict['name']
        except KeyError:
            raise InputDataExcpt(21101)
        #print author_name
        author = book.Author(name=author_name)
        try:
            author.save()
        except IntegrityError:
            raise DatabaseExcp(32101)
          
        dict = {'status': 'ok', 'id': author.id}
    return dict


def book_handler(action, data_dict):
    raise DataExcpt(10000)
    return {'status': 'ok', 'id': 8,}
  

TARGET = {
    'all': all_handler,
    'author': author_handler,
    'book': book_handler, }

