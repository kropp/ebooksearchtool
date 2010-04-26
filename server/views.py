"""opds and xhtml view"""
# -*- coding: utf-8 -*-

from django.core.exceptions import ObjectDoesNotExist

from django.shortcuts import render_to_response

from book.models import Book, Author, Tag, Language
from settings import EBST_NAME, EBST_VERSION, EBST_VERSION_BUILD
from django.template import RequestContext

from django.http import Http404
from django.http import HttpResponse

from django.db.models import Q

from forms.views_forms import ExtendedSearch

EXTENDED_FORM = ExtendedSearch()

def book_request(request, book_id, response_type):
    """ builds opds and xhtml response for book id request"""
    try:
        book = Book.objects.get(id=book_id)
    except ObjectDoesNotExist:
        raise Http404
    
    if response_type == "atom":
        return render_response(request,'book/opds/book.xml',
            {'book': book, })
    if response_type == "xhtml":
        return render_response(request,'book/xhtml/book.xml',
            {'book': book, })

def author_request(request, author_id, response_type):
    """builds opds and xhtml response for all author's books request"""
    try:
        author = Author.objects.get(id=author_id)
    except ObjectDoesNotExist:
        raise Http404
    books = author.book_set.all()
    if response_type == "atom":
        return render_response(request, 'book/opds/author.xml',
        {'author': author, 'books': books})
    if response_type == "xhtml":
        return render_response(request, 'book/xhtml/author.xml',
        {'author': author, 'books': books})

def opensearch_description(request):
    """returns xml open search description"""
    return render_response(request, "data/opensearchdescription.xml", )
    
def catalog(request, response_type):
    """builds opds and xhtml response for catalog request"""
    if response_type == "atom":
        return render_response(request, 'book/opds/catalog.xml')
    if response_type == "xhtml":
        return render_response(request, 'book/xhtml/catalog.xml')
        
def authors_by_one_letter(request, response_type):
    '''returns authors arranged by first letter in their names'''
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
        authors = Author.objects.filter(request_to_server).distinct().\
                                                            order_by('name')
        if response_type == "atom":
            return render_response(request, 'book/opds/books_by_author.xml',
            {'authors': authors})
        if response_type == "xhtml":
            return render_response(request, 'book/xhtml/books_by_author.xml',
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
        return render_response(request, 'book/opds/books_by_author.xml',
        {'string': my_list, 'num': 2, 'letter': letter })
    if response_type == "xhtml":
        return render_response(request, 'book/xhtml/books_by_author.xml',
        {'string': my_list, 'num': 2 })

def authors_by_two_letters(request, response_type):
    '''returns authors arranged by first two letters in their names'''
    if 'letters' in request.GET and request.GET['letters']:
        letters = request.GET['letters']
        try:
            my_let = map(chr, range(ord(letters[1]), ord(letters[4]) + 1))
            request_to_server = Q()
            for let in my_let:
                request_to_server = request_to_server | \
                                    Q(name__istartswith=letters[0]+let)
        except IndexError:
            request_to_server = Q(name__istartswith=letters)

        authors = Author.objects.filter(request_to_server).distinct().\
                                                            order_by('name')

        if response_type == "atom":
            return render_response(request, 'book/opds/books_by_author.xml',
            {'authors': authors})
        if response_type == "xhtml":
            return render_response(request, 'book/xhtml/books_by_author.xml',
            {'authors': authors})
    else:
        alphabet_string = map(chr, range(65, 91))
        request_to_server = Q()
        string = ""
        for let in alphabet_string:
            request_to_server = Q(name__istartswith=let)
            authors_count = Author.objects.filter(request_to_server).\
                                                        distinct().count()
            if authors_count != 0:
                string += let
        if response_type == "atom":
            return render_response(request, 'book/opds/books_by_author.xml',
            {'string': string, 'num': 1 })
        if response_type == "xhtml":
            return render_response(request, 'book/xhtml/books_by_author.xml',
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
        return render_response(request, 'book/opds/books_by_lang.xml',
        {'languages':languages})
    if response_type == "xhtml":
        return render_response(request, 'book/xhtml/books_by_lang.xml',
        {'form':EXTENDED_FORM})

def available_languages():
    '''returns all languages used in books in our data base'''
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
        return render_response(request, 'book/opds/books_by_tag.xml',
        {'tags':tags, 'form':EXTENDED_FORM})
    if response_type == "xhtml":
        return render_response(request, 'book/xhtml/books_by_tag.xml',
        {'tags':tags, 'form':EXTENDED_FORM})
        
def simple_search(request):
    """go to search page"""
    return render_response(request, 'book/xhtml/simple_search.xml')

def extended_search(request):
    """ extended search """
    tags = Tag.objects.all().order_by("name")
    langs = available_languages()
    return render_response(request, 'book/xhtml/extended_search.xml', {'tags': tags,
                                'langs': langs, 'form':EXTENDED_FORM})

def no_book_cover(request):
    '''returns image for books have not self impage'''
    image_data = open("pic/no_cover.gif", "rb").read()
    return HttpResponse(image_data, mimetype="image/png")

def render_response(req, *args, **kwargs):
    bottom_string = "%s v%s (build %s)" % (EBST_NAME, EBST_VERSION, 
                                                        EBST_VERSION_BUILD)
    google_link = 'http://code.google.com/p/ebooksearchtool/'
    kwargs['context_instance'] = RequestContext(req, 
                    {'bottom_string':bottom_string, 'google_link':google_link})

    return render_to_response(*args, **kwargs)
    

def autocomplete_title(request):
    def results_to_string(results):
        if results:
            for r in results:
                yield '%s|%s\n' % (r.title, r.pk)

    query = request.REQUEST.get('q', None)
    author = request.REQUEST.get('author', None)

    if query:
        books = Book.objects.filter(title__istartswith=query)
    else:
        books = Book.objects.all()
    if author:
        books = books.filter(author__name__istartswith=author)        

    books = books[:15]
#    books = books[:int(request.REQUEST.get('limit', 15))]
    return HttpResponse(results_to_string(books), mimetype='text/plain')

def autocomplete_author(request):
    def results_to_string(results):
        if results:
            for r in results:
                yield '%s|%s\n' % (r.name, r.pk)

    query = request.REQUEST.get('q', None)

    if query:
        authors = Author.objects.filter(name__istartswith=query)
    else:
        authors = Author.objects.none()

    authors = authors[:15]
#    books = books[:int(request.REQUEST.get('limit', 15))]
    return HttpResponse(results_to_string(authors), mimetype='text/plain')

