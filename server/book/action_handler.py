'''Action handler'''


import server.book.models as book
from server.exception import *
from server.book.models import Alias
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
        "Save entirety author to database"
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
    def __init__(self, link, size, type='',
                 more_info='', img_link=''):
        self.link = link
        self.size = size
        self.type = type
        self.more_info = more_info
        self.img_link = img_link

    def save_to_db(self):
        pass

class BookEntirety:
    def __init__(self, title, authors, files, annotations):
        self.title = title
        self.authors = authors
        self.files = files
        self.annotations = annotations

    def save_to_db(self):
        for author in authors:
            author.save_to_db()



def get_all_handler(data_dict):
    "Get book"
    a = AuthorEntirety()
    print a.get_from_db()


def insert_all_handler(data_dict):
    print data_dict['author']
    a = AuthorEntirety('author5', ['author4alias1', 'author4alias2'])
    a.save_to_db()



@transaction.commit_manually
def all_handler(action, data_dict):
    "Handler of all requests, maintains the integrity of database"
    if action == ACTION['get']:
        return get_all_handler(data_dict)
    elif action == ACTION['insert']:
        try:
            dict = insert_all_handler(data_dict)
        except Exception, ex:
            transaction.rollback()
            raise ex
        transaction.commit()
        return dict
    else:
        raise DataExcpt(40001)






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

