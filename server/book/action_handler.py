'''Action handler'''

import server.book.models as book
from server.exception import *

def author_handler(action, data_list):
  return {'status': 'ok', 'id': 8,}

#def add_author_db(name, alias=[]):
#  if name == '':
#    raise InputDataExcpt(22101)
#
#  alias = map(convert_delim, alias)
#  name = convert_delim(name)
#
#  author = book.Author(name=name)
#  try:
#    author.save()
#  except IntegrityError:
#    raise InputDataExcpt(32101)
#
#  return 0

def book_handler(action, data_list):
  raise DataExcpt(10000)
  return {'status': 'ok', 'id': 8,}
  
ACTION = {
    'get': 1,
    'insert': 2,
    'update': 3,
    'remove': 4,}

TARGET = {
    'author': author_handler,
    'book': book_handler, }

