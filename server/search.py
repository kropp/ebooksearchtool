""" search module """

from django.db.models import Q

from book.models import Book, Author, Tag, Language
from django.shortcuts import render_to_response
from django.template import RequestContext
from django.core.exceptions import ObjectDoesNotExist

from book.search import SphinxSearchEngine

# type of search engine
search_engine = SphinxSearchEngine()

def simple_search(request, response_type, items_per_page, page, start_index):
    """ simple search with query """
    tags = Tag.objects.all().order_by("name")
    query = request.GET['query']

    books = search_engine.simple_search(query)
    
    authors = search_engine.author_search(author=query, max_length=5)         

    total = len(books)
    # + search in annotation

    next = None
    if (total-1)/items_per_page != 0:
        next = page+1

    if response_type == "atom":
        return render_to_response('book/opds/search_response.xml',
            {'books': books[start_index:start_index+items_per_page], 'query': query, 'curr': page,
            'items_per_page': items_per_page, 'total':total, 'next':next, },
            context_instance=RequestContext(request))
        
    if response_type == "xhtml":
        return render_to_response('book/xhtml/search_response.xml',
            {'books': books,'items_per_page': items_per_page, 'query': query,
            'tags': tags}, context_instance=RequestContext(request))

def search_in_author(request, lang, tag, response_type, items_per_page, page, 
                                start_index, main_title):
    tags = Tag.objects.all().order_by("name")
    author = request.GET['author']
    #TODO language in author_search
    authors = search_engine.author_search(author=author, lang=lang, tag=tag, max_length=10)
    total = len(authors)
    next = None
    if (total-1)/items_per_page != 0:
        next = page+1
    if response_type == "atom":
        return render_to_response('book/opds/authors_search_response.xml',
            {'authors': authors[start_index:start_index+items_per_page],
            'title': main_title,  'curr': page, 'next':next, 'author': author, 
            'items_per_page':items_per_page, 'total':total })
    if response_type == "xhtml":
        return render_to_response('book/xhtml/authors_search_response.xml',
            {'authors': authors, 'author': author, 
            'items_per_page':items_per_page, 'tags': tags}, 
            context_instance=RequestContext(request))


def search_request_to_server(request, response_type, is_all):
    """ builds opds and xhtml response for search request"""
    tags = Tag.objects.all().order_by("name")
    request_to_server = Q()
    main_title = {}
    
    page, start_index, items_per_page = 1, 0, 20
    title = author = tag = lang = None
    books = Book.objects.none()

    # initialization
    if 'items_per_page' in request.GET and request.GET['items_per_page']:
        items_per_page = int(request.GET['items_per_page'])
    
    if 'page' in request.GET and request.GET['page']:
        page = int(request.GET['page'])
        start_index = items_per_page * (page - 1)

    if 'title' in request.GET and request.GET['title']:
        title = request.GET['title']

    if 'author' in request.GET and request.GET['author']:
        author = request.GET['author']

    if 'lang' in request.GET and request.GET['lang']:
        lang = request.GET['lang']
        request_to_server = request_to_server & Q(lang=lang)
        main_title['lang'] = lang

    if 'tag' in request.GET and request.GET['tag']:
        tag = request.GET['tag']
        request_to_server = request_to_server & Q(tag__name__icontains=tag)
        main_title['tag'] = tag

    # main logic
    if 'query' in request.GET and request.GET['query']:
        # search in title, author.name, alias, annotation
        return simple_search(request, response_type, items_per_page, page, 
                                start_index)
    if title:
        main_title['tit'] = title
        if author:
            main_title['author'] = author    
        books = search_engine.book_search(title=title, author=author, tag=tag, lang=lang)          
    
    elif author:
        return search_in_author(request, lang, tag, response_type, items_per_page, page, 
                                start_index, main_title)
    else:
        books = Book.objects.filter(request_to_server).distinct()

    if is_all == "yes":
        books = Book.objects.all()

    total = len(books)

    next = None
    if (total-1)/items_per_page != 0:
        next = page+1
        
    if response_type == "atom":
        return render_to_response('book/opds/search_response.xml',
            {'books': books[start_index:start_index+items_per_page],
            'title': main_title,  'curr': page, 'next':next,
            'items_per_page':items_per_page, 'total':total })
        
    if response_type == "xhtml":
        return render_to_response('book/xhtml/search_response.xml',
            {'books': books, 'title': main_title, 'items_per_page':items_per_page, 
            'tags': tags}, context_instance=RequestContext(request))


