"""opds and xhtml view"""
# -*- coding: utf-8 -*-

from django.core.exceptions import ObjectDoesNotExist
from django.db.models import Q
from django.shortcuts import render_to_response
from string import split

from book.models import Book
from book.models import Author
from book.models import Tag

from django.http import Http404
from django.http import HttpResponse

def search_request_to_server(request, response_type, is_all):
    """ builds opds and xhtml response for search request"""
    try:
        is_all = request.GET['is_all']
    except KeyError:
        pass
          
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
    main_title = {}
    try:
    # search in title, author.name, alias, annotation, more_info (book_file)
        query = request.GET['query']
        main_title['query'] = query
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
        if title != '':
            request_to_server = request_to_server & Q(title__icontains=title)
            main_title['tit'] = title
    except KeyError:
        title = None        

    try:
    # search in author.name, alias
        author = request.GET['author']
        if author != '':
            request_to_server = request_to_server\
                          & (Q(author__name__icontains=author) \
                          | Q(author__alias__name__icontains=author))
            main_title['author'] = author
    except KeyError:
        author = None    
        
    try:
    # search in lang
        lang = request.GET['lang']
        if lang != '':
            request_to_server = request_to_server & Q(lang=lang)
            main_title['lang'] = lang
    except KeyError:
        lang = None   

    try:
    # search in tag
        tag = request.GET['tag']
        if tag != '':
            request_to_server = request_to_server & Q(tag__name__icontains=tag)
            main_title['tag'] = tag
    except KeyError:
        tag = None          

    books = Book.objects.filter(request_to_server).distinct()
    
    if len(request_to_server) == 0:
        books = Book.objects.none()

    if is_all == "yes":
        query = None
        author = None
        title = None
        
        books = Book.objects.all()
        
    total = books.count()

    seq = range(1, total/items_per_page+2)
    
    c = total/items_per_page+2
    if c > 5:
        seq = [1, 2, total/items_per_page+2]
    
    if total%items_per_page == 0:
        seq = range(1, total/items_per_page+1)
        
    if seq.__len__() == 1:
        next = 0
    
    if response_type == "atom":
        return render_to_response('book/opds/client_response_search.xml',
            {'books': books[start_index:start_index+items_per_page],
            'title': main_title,  'curr': next - 1,
            'items_per_page':items_per_page, 'total':total, 'next':next, })
        
    if response_type == "xhtml":
        return render_to_response('book/xhtml/client_response_search.xml',
            {'books': books[start_index:start_index+items_per_page],
            'title': main_title, 'total':total,
            'items_per_page':items_per_page, 'next':next, 'curr': next - 1,
            'seq':seq, })

def book_request_to_server(request, book_id, response_type):
    """ builds opds and xhtml response for book id request"""
    try:
        book = Book.objects.get(id=book_id)
    except ObjectDoesNotExist:
        raise Http404
    
    if response_type == "atom":
        return render_to_response('book/opds/client_response_book.xml',
            {'book': book, })
    if response_type == "xhtml":
        return render_to_response('book/xhtml/client_response_book.xml',
            {'book': book, })

def author_request_to_server(request, author_id, response_type):
    """builds opds and xhtml response for all author's books request"""
    try:
        author = Author.objects.get(id=author_id)
    except ObjectDoesNotExist:
        raise Http404
    if response_type == "atom":
        return render_to_response('book/opds/client_response_author_books.xml',
        {'author': author})
    if response_type == "xhtml":
        return render_to_response('book/xhtml/client_response_author_books.xml',
        {'author': author})

def opensearch_description(request):
    """returns xml open search description"""
    return render_to_response("data/opensearchdescription.xml", )
    
def catalog_request_to_server(request, response_type):
    """builds opds and xhtml response for catalog request"""
    if response_type == "atom":
        return render_to_response('book/opds/client_response_catalog.xml')
    if response_type == "xhtml":
        return render_to_response('book/xhtml/client_response_catalog.xml')
        
def books_by_authors_request_to_server(request, response_type):
    """builds opds and xhtml response for authors by letter request"""
    try:
        letter = request.GET['letter']
    except KeyError:
        alphabet_string = "A B C D E F G H I J K L M N O P Q R S T U V W X Y Z"
        if response_type == "atom":
            return render_to_response('book/opds/client_response_books_by_author.xml',
            {'alphabet': alphabet_string})
        if response_type == "xhtml":
            return render_to_response('book/xhtml/client_response_books_by_author.xml',
            {'alphabet': alphabet_string})        
        
    request_to_server = Q(name__istartswith=letter)
    authors = Author.objects.filter(request_to_server).distinct()
    if response_type == "atom":
        return render_to_response('book/opds/client_response_books_by_author_letter.xml',
        {'authors': authors})
    if response_type == "xhtml":
        return render_to_response('book/xhtml/client_response_books_by_author_letter.xml',
        {'authors': authors})
            
def books_by_languages_request_to_server(request, response_type):
    """builds opds and xhtml response for books by lang request"""
    lang = Book.objects.values_list('lang')
    languages = set(lang)
    languages =  [x[0] for x in languages]
    if response_type == "atom":
        return render_to_response('book/opds/client_response_books_by_lang.xml',
        {'languages':languages})
    if response_type == "xhtml":            
        return render_to_response('book/xhtml/client_response_books_by_lang.xml',
        {'languages':languages})
        
def books_by_tags_request_to_server(request, response_type):
    """builds opds and xhtml response for books by tags request"""
    tags = Tag.objects.all()
    if response_type == "atom":
        return render_to_response('book/opds/client_response_books_by_tag.xml',
        {'tags':tags})
    if response_type == "xhtml":
        return render_to_response('book/xhtml/client_response_books_by_tag.xml',
        {'tags':tags})
        
def books_search(request):
    """go to search page"""
    return render_to_response('book/xhtml/client_response_search_request.xml')
    
def no_book_cover(request):
    image_data = open("pic/no_cover.gif", "rb").read()
    return HttpResponse(image_data, mimetype="image/png")

def extended_search(request):
    """ extended search """
    return render_to_response('book/xhtml/extended_search.xml')
