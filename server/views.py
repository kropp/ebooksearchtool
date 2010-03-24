"""opds and xhtml view"""
# -*- coding: utf-8 -*-

from django.core.exceptions import ObjectDoesNotExist

from django.shortcuts import render_to_response
from string import split

from book.models import Book, Author, Tag, Language

from django.http import Http404
from django.http import HttpResponse

from search import *

import os

def book_request(request, book_id, response_type):
    """ builds opds and xhtml response for book id request"""
    try:
        book = Book.objects.get(id=book_id)
    except ObjectDoesNotExist:
        raise Http404
    
    if response_type == "atom":
        return render_to_response('book/opds/client_response_book.xml',
            {'book': book, })
    if response_type == "xhtml":
        return render_to_response('book/xhtml/book.xml',
            {'book': book, })

def author_request(request, author_id, response_type):
    """builds opds and xhtml response for all author's books request"""
    try:
        author = Author.objects.get(id=author_id)
    except ObjectDoesNotExist:
        raise Http404
    if response_type == "atom":
        return render_to_response('book/opds/client_response_author_books.xml',
        {'author': author})
    if response_type == "xhtml":
        return render_to_response('book/xhtml/author.xml',
        {'author': author})

def opensearch_description(request):
    """returns xml open search description"""
    return render_to_response("data/opensearchdescription.xml", )
    
def catalog(request, response_type):
    """builds opds and xhtml response for catalog request"""
    if response_type == "atom":
        return render_to_response('book/opds/catalog.xml')
    if response_type == "xhtml":
        return render_to_response('book/xhtml/catalog.xml')
        
def authors_by_one_letter(request, response_type):
    letter = request.GET['letter']
    alphabet_string = map(chr, range(98, 122))
    string = ""
    for let in alphabet_string:
        request_to_server = Q(name__istartswith=letter+let)
        auth_count = Author.objects.filter(request_to_server).distinct().count()
        if auth_count > 0:          # TODO condition
           string += let
    string = string[0:-1]
    
    if len(string) < 1:
        request_to_server = Q(name__istartswith=letter)
        authors = Author.objects.filter(request_to_server).distinct().order_by('name')
        if response_type == "atom":
            return render_to_response('book/opds/client_response_books_by_author_letter.xml',
            {'authors': authors})
        if response_type == "xhtml":
            return render_to_response('book/xhtml/books_by_author.xml',
            {'authors': authors}) 
    
    my_list = []
    my_string = letter+'a'
    for let in string:
        if my_string != letter+let:
            my_string += "-" + letter + let 
        my_list.append(my_string)
        my_string = letter+chr(ord(let)+1)
    my_string += "-" + letter+"z"
    my_list.append(my_string)
    
    if response_type == "atom":
        return render_to_response('book/opds/client_response_books_by_author.xml',
        {'string': my_list, 'num': 2, 'letter': letter })
    if response_type == "xhtml":
        return render_to_response('book/xhtml/books_by_author.xml',
        {'string': my_list, 'num': 2 })        

def authors_by_two_letters(request, response_type):
    if 'letters' in request.GET and request.GET['letters']:
        letters = request.GET['letters']
        try:
            my_let = map(chr, range(ord(letters[1]), ord(letters[4]) + 1))
            request_to_server = Q()
            for let in my_let:
                request_to_server = request_to_server | Q(name__istartswith=letters[0]+let)
        except IndexError:
            request_to_server = Q(name__istartswith=letters)

        authors = Author.objects.filter(request_to_server).distinct().order_by('name')

        if response_type == "atom":
            return render_to_response('book/opds/client_response_books_by_author_letter.xml',
            {'authors': authors})
        if response_type == "xhtml":
            return render_to_response('book/xhtml/books_by_author.xml',
            {'authors': authors}) 
    else:
        alphabet_string = map(chr, range(65, 91))
        request_to_server = Q()
        string = ""
        for let in alphabet_string:
            request_to_server = Q(name__istartswith=let)
            authors_count = Author.objects.filter(request_to_server).distinct().count()
            if authors_count != 0:
                string += let
        if response_type == "atom":
            return render_to_response('book/opds/client_response_books_by_author.xml',
            {'string': string, 'num': 1 })
        if response_type == "xhtml":
            return render_to_response('book/xhtml/books_by_author.xml',
            {'string': string, 'num': 1 }) 

def books_by_authors(request, response_type):
    """builds opds and xhtml response for authors by letter request"""

    if 'letter' in request.GET and request.GET['letter']:
        return authors_by_one_letter(request, response_type)
    else:
        return authors_by_two_letters(request, response_type)
                    
def books_by_language(request, response_type):
    """builds opds and xhtml response for books by lang request"""
    languages = available_languages()
    if response_type == "atom":
        return render_to_response('book/opds/client_response_books_by_lang.xml',
        {'languages':languages})
    if response_type == "xhtml":            
        return render_to_response('book/xhtml/books_by_lang.xml',
        {'languages':languages})

def available_languages(): 
    lang = Book.objects.values_list('lang')
    short_langs = set(lang)
    short_langs =  [x[0] for x in short_langs]

    languages = set()
    for short_lang in short_langs:
        language = Language.objects.filter(short=short_lang).order_by('full')
        if language:
            languages.add((language[0].full, short_lang))    
    return sorted(languages)    

def books_by_tags(request, response_type):
    """builds opds and xhtml response for books by tags request"""
    tags = Tag.objects.all().order_by("name")
    if response_type == "atom":
        return render_to_response('book/opds/client_response_books_by_tag.xml',
        {'tags':tags})
    if response_type == "xhtml":
        return render_to_response('book/xhtml/books_by_tag.xml',
        {'tags':tags})
        
def simple_search(request):
    """go to search page"""
    return render_to_response('book/xhtml/simple_search.xml')

def extended_search(request):
    """ extended search """
    tags = Tag.objects.all().order_by("name")
    return render_to_response('book/xhtml/extended_search.xml', {'tags': tags})

def no_book_cover(request):
    image_data = open("pic/no_cover.gif", "rb").read()
    return HttpResponse(image_data, mimetype="image/png")
