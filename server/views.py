# -*- coding: utf-8 -*-

from django.core.exceptions import *
from django.db.models import Q
from django.shortcuts import render_to_response
from string import split

from book.models import *
from spec.utils import SERVER_URL


def search_to_opds(query, title, author, books, items_per_page, total, next, start_index):

    return render_to_response('opds/client_response_search.xml', {'books': books, 'query': query, 'title': title, 'author':author, 'server':SERVER_URL, 'items_per_page':items_per_page, 'total':total, 'next':next, 'start_index': start_index })

def book_to_opds(book):

    return render_to_response('opds/client_response_book.xml', {'book': book, 'server':SERVER_URL})

def author_to_opds(author):

    return render_to_response('opds/client_response_author.xml', {'author': author, 'server':SERVER_URL})

def search_request_to_server(request):
    try:
        items_per_page = int(request.GET['items_per_page'])
    except KeyError:
        items_per_page = 20
    
    try:
        page = int(request.GET['page'])
        start_index = items_per_page * page
    except KeyError:
        page = 1
        start_index = 0
    next = page + 1
    q = Q()
    try:
    # search in title, author.name, alias, annotation, more_info (book_file)
        query = request.GET['query']
        for word in query.split():
            q = q | Q(title__icontains=word) \
              | Q(author__name__icontains=word) \
              | Q(author__alias__name__icontains=word) \
              | Q(annotation__name__icontains=word) \
              | Q(book_file__more_info__icontains=word)
    except KeyError:
        query = None    
    try:
    # search in title
        title = request.GET['title']
        q = q & Q(title__icontains=title) 
    except KeyError:
        title = None        

    try:
    # search in author.name, alias
        author = request.GET['author']
        q = q & (Q(author__name__icontains=author) \
          | Q(author__alias__name__icontains=author))
    except KeyError:
        author = None        

    books = Book.objects.filter(q).distinct()
    total = books.count()
#    print books
#    print books.count()
    
    return search_to_opds(query, title, author, books[start_index:start_index+items_per_page], items_per_page, total, next, start_index)

def book_request_to_server(request, book_id):
    try:
        book = Book.objects.get(id=book_id)
    except ObjectDoesNotExist:
        pass
    
    return book_to_opds(book)

def author_request_to_server(request, author_id):
    try:
        author = Author.objects.get(id=author_id)
    except ObjectDoesNotExist:
        pass
    
    return author_to_opds(author)

def opensearch_description(request):
    return render_to_response("data/opensearchdescription.xml")
