""" search module """

from django.db.models import Q

from book.models import Book, Author, Tag, Language
from django.shortcuts import render_to_response
from django.template import RequestContext
from django.core.exceptions import ObjectDoesNotExist

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

def search_in_title(title, author, tag, lang):
    books = Book.title_search.query(title)
    if author:
        authors = Author.soundex_search.query(author)
        authors_id = map(lambda x: x.id, authors)    
        books = books.filter(author_id=authors_id)
    if tag:
        try:
            tags_id = Tag.objects.get(name=tag).id
            books = books.filter(tag_id=tags_id)
        except ObjectDoesNotExist:
            pass                
    if lang:
        try:
            lang_id = Language.objects.get(short=lang).id
            books = books.filter(language_id=lang_id)
        except ObjectDoesNotExist:
            pass 
    return books

def search_in_author(request, response_type, items_per_page, page, 
                                start_index, main_title):
    tags = Tag.objects.all().order_by("name")
    author = request.GET['author']
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


def search_request_to_server(request, response_type, is_all):
    """ builds opds and xhtml response for search request"""
    tags = Tag.objects.all().order_by("name")
    request_to_server = Q()
    main_title = {}
    
    page, start_index, items_per_page = 1, 0, 20
    title = author = tag = lang = None

    # initialization
    if 'items_per_page' in request.GET and request.GET['items_per_page']:
        items_per_page = int(request.GET['items_per_page'])
    
    if 'page' in request.GET and request.GET['page']:
        page = int(request.GET['page'])
        start_index = items_per_page * (page - 1)

    if 'query' in request.GET and request.GET['query']:
        # search in title, author.name, alias, annotation
        return simple_search(request, response_type, items_per_page, page, 
                                start_index)        

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


    if title:
        main_title['tit'] = title
        if author:
            main_title['author'] = author
        books = search_in_title(title, author, tag, lang) 
        total = books.count()
    
    elif tag:
        if author:
            books = Book.objects.all()
            main_title['author'] = author
            authors = Author.soundex_search.query(author)
            authors_id = map(lambda x: x.id, authors)    
            books = books.filter(author__id__in=authors_id)
            try:
                tags_id = Tag.objects.get(name=tag).id
                books = books.filter(tag_id=tags_id)
            except ObjectDoesNotExist:
                pass                
            if lang:
                try:
                    lang_id = Language.objects.get(short=lang).id
                    books = books.filter(language_id=lang_id)
                except ObjectDoesNotExist:
                    pass  
        else:
            books = Book.objects.filter(request_to_server).distinct()

    elif lang:
        if author:
            books = Book.objects.all()
            main_title['author'] = author
            authors = Author.soundex_search.query(author)
            authors_id = map(lambda x: x.id, authors)    
            books = books.filter(author__id__in=authors_id)
            try:
                lang_id = Language.objects.get(short=lang).id
                books = books.filter(language_id=lang_id)
            except ObjectDoesNotExist:
                pass                
        else:
            books = Book.objects.filter(request_to_server).distinct()

    elif author:
        return search_in_author(request, response_type, items_per_page, page, 
                                start_index, main_title)

    else:
        books = Book.objects.none()

    if is_all == "yes":
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


