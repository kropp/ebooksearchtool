# -*- coding: utf-8 -*-

from django.core.exceptions import *
from django.db.models import Q
from django.shortcuts import render_to_response

from book.models import *
from spec.utils import SERVER_URL


def search_to_opds(query, books, items_per_page, total, next):

    return render_to_response('opds/client_response_search.xml', {'books': books, 'query': query, 'server':SERVER_URL, 'items_per_page':items_per_page, 'total':total, 'next':next})

def book_to_opds(book):

    return render_to_response('opds/client_response_book.xml', {'book': book,'server':SERVER_URL})

def search_request_to_server(request):
    query = request.GET['query']

    try:
        items_per_page = int(request.GET['items_per_page'])
    except KeyError:
        items_per_page = 20
    
    try:
        page = int(request.GET['page'])
        i = items_per_page * page
    except KeyError:
        page = 1
        i = 0
        
    next = page + 1

    # search in title, author.name, alias, anntation, more_info (book_file)
    q = Q(title__icontains=query) \
      | Q(author__name__icontains=query)# \
  #    | Q(author__alias__name__icontains=query) \
  #    | Q(annotation__name__icontains=query) \
  #    | Q(book_file__more_info__icontains=query)

    books = Book.objects.filter(q).distinct()
    total = books.count()
    print books
    print books.count()
    
    return search_to_opds(query, books[i:i+items_per_page], items_per_page, total, next)

def book_request_to_server(request, book_id):
    try:
        book = Book.objects.get(id=book_id)
    except ObjectDoesNotExist:
        pass
    
    return book_to_opds(book)

