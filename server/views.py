"""opds and xhtml view"""
# -*- coding: utf-8 -*-

from django.core.exceptions import ObjectDoesNotExist
from django.db.models import Q
from django.shortcuts import render_to_response
from string import split

from book.models import Book
from book.models import Author
from spec.utils import SERVER_URL


def search_request_to_server(request, response_type):
    """ builds opds and xhtml response for search request"""
    try:
        items_per_page = int(request.GET['items_per_page'])
    except KeyError:
        items_per_page = 20
    
    try:
        page = int(request.GET['page'])
        start_index = items_per_page * (page - 1)
    except KeyError:
        page = 1
        start_index = 0
    next = page + 1
    request_to_server = Q()
    try:
    # search in title, author.name, alias, annotation, more_info (book_file)
        query = request.GET['query']
        for word in query.split():
            request_to_server = request_to_server | Q(title__icontains=word) \
              | Q(author__name__icontains=word) \
              | Q(author__alias__name__icontains=word) \
              | Q(annotation__name__icontains=word) \
              | Q(book_file__more_info__icontains=word)
    except KeyError:
        query = None    
    try:
    # search in title
        title = request.GET['title']
        request_to_server = request_to_server & Q(title__icontains=title) 
    except KeyError:
        title = None        

    try:
    # search in author.name, alias
        author = request.GET['author']
        request_to_server = request_to_server\
         & (Q(author__name__icontains=author) \
         | Q(author__alias__name__icontains=author))
    except KeyError:
        author = None        

    books = Book.objects.filter(request_to_server).distinct()
    
    if len(request_to_server) == 0:
        books = Book.objects.none()
        
    total = books.count()
    seq = range(1, total/items_per_page+2)
    
    if response_type == "atom":
        return render_to_response('book/opds/client_response_search.xml', 
            {'books': books[start_index:start_index+items_per_page], 
            'query': query, 'title': title, 'author':author,  'curr': next - 1, 
            'items_per_page':items_per_page, 'total':total, 'next':next, 
            'server':SERVER_URL })
        
    if response_type == "xhtml":
        return render_to_response('book/xhtml/client_response_search.xml', 
            {'books': books[start_index:start_index+items_per_page], 
            'query': query, 'title': title, 'author':author, 'total':total,
            'items_per_page':items_per_page, 'next':next, 'curr': next - 1, 
            'seq':seq, 'server':SERVER_URL })

def book_request_to_server(request, book_id, response_type):
    """ builds opds and xhtml response for book id request"""
    try:
        book = Book.objects.get(id=book_id)
    except ObjectDoesNotExist:
        pass
    
    if response_type == "atom":
        return render_to_response('book/opds/client_response_book.xml', 
            {'book': book, 'server':SERVER_URL})
    if response_type == "xhtml":
        return render_to_response('book/xhtml/client_response_book.xml', 
            {'book': book, 'server':SERVER_URL})

def author_request_to_server(request, author_id, response_type):
    """ builds opds and xhtml response for author id request"""
    try:
        author = Author.objects.get(id=author_id)
    except ObjectDoesNotExist:
        pass
    if response_type == "atom":
        return render_to_response('book/opds/client_response_author.xml', 
            {'author': author, 'server':SERVER_URL})
    if response_type == "xhtml":
        return render_to_response('book/xhtml/client_response_author.xml', 
            {'author': author, 'server':SERVER_URL})

def opensearch_description(request):
    """returns xml open search description"""
    return render_to_response("data/opensearchdescription.xml", 
        {'server':SERVER_URL})
    
def all_books_request_to_server(request, response_type):
    """builds opds and xhtml response for all books request"""
    try:
        items_per_page = int(request.GET['items_per_page'])
    except KeyError:
        items_per_page = 20
    
    try:
        page = int(request.GET['page'])
        start_index = items_per_page * (page - 1)
    except KeyError:
        page = 1
        start_index = 0
    next = page + 1
    query = None
    author = None
    title = None
    
    books = Book.objects.all()
    
    total = books.count()
    
    seq = range(1, total/items_per_page+1)
    
    if response_type == "atom":
        return render_to_response('book/opds/client_response_search.xml', 
            {'books': books[start_index:start_index+items_per_page], 
            'query': query, 'title': title, 'author':author, 'total':total,
            'server':SERVER_URL, 'items_per_page':items_per_page,  'next':next, 
            'curr': next - 1 })
        
    if response_type == "xhtml":
        return render_to_response('book/xhtml/client_response_search.xml', 
        {'books': books[start_index:start_index+items_per_page], 'query': query,
         'title': title, 'author':author, 'server':SERVER_URL, 'total':total,
         'items_per_page':items_per_page, 'next':next, 'curr': next - 1, 
         'seq':seq, })

