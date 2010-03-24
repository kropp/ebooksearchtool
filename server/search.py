""" search module """

from django.db.models import Q

from book.models import Book, Author, Tag
from django.shortcuts import render_to_response
from django.template import RequestContext

def get_page_range(page, total, items_per_page):
    if page <= 10:
        if total/items_per_page+2 > 10:
            return range(1, page + 10)
        else:
            if total%items_per_page == 0:
                return range(1, total/items_per_page+1)
            else:
                return range(1, total/items_per_page+2)
            
    if page > 10:
        if total/items_per_page+2 > page + 10:
            return range(page - 10, page + 10)
        else:
            if total%items_per_page == 0:
                return range(page - 10, total/items_per_page+1)
            else:
                return range(page - 10, total/items_per_page+2)

def simple_search(request, response_type, items_per_page, page, start_index):
    """ simple search with query """
    tags = Tag.objects.all().order_by("name")
    query = request.GET['query']

    authors = Author.soundex_search.query(query)            # TODO sort authors by distance
    authors_id = map(lambda x: x.id, authors)
    books_simple = Book.title_search.query(query)
    books_filtered = books_simple.filter(author_id=authors_id)
    books = books_filtered[0:books_filtered.count()]
    books.extend(books_simple)             # TODO merge books_simple and books_filtered
    total = len(books)
    # + search in annotation

    next = None
    if total-1/items_per_page != 0:
        next = page+1

    if response_type == "atom":
        return render_to_response('book/opds/search_response.xml',
            {'books': books[start_index:start_index+items_per_page], 'query': query, 'curr': page,
            'items_per_page': items_per_page, 'total':total, 'next':next, },
            context_instance=RequestContext(request))
        
    if response_type == "xhtml":
        return render_to_response('book/xhtml/search_response.xml',
            {'books': books,'items_per_page': items_per_page, 'query': query,
            'authors': authors[0:5], 'tags': tags}, context_instance=RequestContext(request))

def search_request_to_server(request, response_type, is_all):
    """ builds opds and xhtml response for search request"""
    tags = Tag.objects.all().order_by("name")

    if 'items_per_page' in request.GET and request.GET['items_per_page']:
        items_per_page = int(request.GET['items_per_page'])
    else:
        items_per_page = 20
    
    if 'page' in request.GET and request.GET['page']:
        page = int(request.GET['page'])
        start_index = items_per_page * (page - 1)
    else:
        page = 1
        start_index = 0

    request_to_server = Q()
    main_title = {}

    if 'query' in request.GET and request.GET['query']:
        # search in title, author.name, alias, annotation
        return simple_search(request, response_type, items_per_page, page, 
                                start_index)        
    if 'title' in request.GET and request.GET['title']:
    # search in title
        title = request.GET['title']
    else:
        title = None        

    if 'author' in request.GET and request.GET['author']:
    # search in author.name, alias
        author = request.GET['author']
    else:
        author = None    

    if 'lang' in request.GET and request.GET['lang']:
    # search in lang
        lang = request.GET['lang']
        request_to_server = request_to_server & Q(lang=lang)
        main_title['lang'] = lang
    else:
        lang = None   

    if 'tag' in request.GET and request.GET['tag']:
    # search in tag
        tag = request.GET['tag']
        request_to_server = request_to_server & Q(tag__name__icontains=tag)
        main_title['tag'] = tag
    else:
        tag = None          

    if title:
        books = Book.title_search.query(title)
        if author:
            authors = Author.soundex_search.query(author)
            authors_id = map(lambda x: x.id, authors)    
            books = books.filter(author_id=authors_id)
        else:
            main_title['tit'] = title
        total = books.count()
    elif author:
        authors = Author.soundex_search.query(author)
        total = authors.count()
        next = None
        if total-1/items_per_page != 0:
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

    elif tag or lang:        
        books = Book.objects.filter(request_to_server).distinct()
        total = books.count()
        if len(request_to_server) == 0:
            books = Book.objects.none()
    else:
        books = Book.objects.none()
        total = 0

    if is_all == "yes":
        query = author = title = None
        
        books = Book.objects.all()
        total = books.count()

    next = None
    if total-1/items_per_page != 0:
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


