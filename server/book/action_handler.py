'''Action handler'''


import server.book.models as book
from server.exception import *
from django.db import IntegrityError

ACTION = {
    'get': 1,
    'insert': 2,
    'update': 3,
    'remove': 4,}

def author_handler(action, data_dict):
    print data_dict
    if action == ACTION['insert']:
        try:
            author_name = data_dict['name']
        except KeyError:
            raise InputDataExcpt(21101)
        print author_name
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
    'author': author_handler,
    'book': book_handler, }

