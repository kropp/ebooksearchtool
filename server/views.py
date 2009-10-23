# -*- coding: utf-8 -*-
from django.contrib.syndication.feeds import Feed
from book.models import *
from django.utils.feedgenerator import Atom1Feed
from django.utils.xmlutils import SimplerXMLGenerator
from django.core.exceptions import *

from spec.utils import SERVER_URL

from django.http import HttpResponse

from django.shortcuts import render_to_response

def my_test(request, add_author = ''):
 #   a = Author()
 #   a.name = u"Ремарк, Эрих Мария"
 #   a.save()
    if add_author:
      a = Author()
      a.name = add_author
      a.save()

    a_list = Author.objects.all()
    html = ''
    for a in a_list:
        html += a.name + "<br>"
    
    return HttpResponse(html)

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
    books = Book.objects.filter(title__icontains=query)
    total = books.count()
    return search_to_opds(query, books[i:i+items_per_page], items_per_page, total, next)

def book_request_to_server(request, book_id):
    try:
        book = Book.objects.get(id=book_id)
    except ObjectDoesNotExist:
        pass
    
    return book_to_opds(book)

