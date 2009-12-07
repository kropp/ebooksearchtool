from django.conf.urls.defaults import include
from django.conf.urls.defaults import patterns
from django.contrib import admin

from book.views import ACTION, data_modify

import views


urlpatterns = patterns('',

    # interface for analizer/crawler
    (r'^data/get/?$', data_modify, {'action': ACTION['get'],}),
    (r'^data/insert/?$', data_modify, {'action': ACTION['insert'],}),

    # interface for search
    (r'^search.atom/?$', 'views.search_request_to_server',
        {'response_type': 'atom', 'is_all': 'no'}),
    (r'^search/?$', 'views.search_request_to_server',
        {'response_type': 'xhtml', 'is_all': 'no'}),
        
    #all books
    (r'^all.atom/?$', 'views.search_request_to_server',
        {'response_type': 'atom', 'is_all': 'yes'}),
    (r'^all/?$', 'views.search_request_to_server',
        {'response_type': 'xhtml', 'is_all': 'yes'}),        
    
    # requests
    (r'^book.atom/id(\d{1,})/?$', 'views.book_request_to_server',
        {'response_type': 'atom',}),
    (r'^book/id(\d{1,})/?$', 'views.book_request_to_server',
        {'response_type': 'xhtml',}),
    
    (r'^author.atom/id(\d{1,})/?$', 'views.author_request_to_server',
        {'response_type': 'atom',}),
    (r'^author/id(\d{1,})/?$', 'views.author_request_to_server',
        {'response_type': 'xhtml',}),
    
    #book catalog
    (r'^catalog.atom/?$', 'server.views.catalog_request_to_server',
        {'response_type': 'atom',}),
    (r'^catalog/?$', 'server.views.catalog_request_to_server',
        {'response_type': 'xhtml',}),        
    
    #books sorted by authors, languages, subjects
    (r'^discover/authors.atom/?$', 'views.books_by_authors_request_to_server',
        {'response_type': 'atom',}),
    (r'^discover/authors/?$', 'views.books_by_authors_request_to_server',
        {'response_type': 'xhtml',}),
        
    (r'^discover/languages.atom/?$', 'views.books_by_languages_request_to_server',
        {'response_type': 'atom',}),
    (r'^discover/languages/?$', 'views.books_by_languages_request_to_server',
        {'response_type': 'xhtml',}),        
        
    (r'^discover/subjects.atom/?$', 'views.books_by_tags_request_to_server',
        {'response_type': 'atom',}),
    (r'^discover/subjects/?$', 'views.books_by_tags_request_to_server',
        {'response_type': 'xhtml',}),   
        
    (r'^discover/search/?$', 'views.books_search'),  
    
    # no book cover available
    (r'^pic/nobookcover/?$', 'views.no_book_cover'),         
    
    #admin
    (r'^admin/(.*)', admin.site.root),
#    (r'^admin/doc/', include('django.contrib.admindocs.urls')),
    
    #opensearchdescription
    (r'^opensearch/?$', 'server.views.opensearch_description'),
    
    #user_information
#    (r'^user/insert/?$', 'reader.views.insert_user_information'),
    
    #openid
    (r'^$', 'django.views.generic.simple.direct_to_template',
        {'template': 'home.html'}),
    (r'^account/', include('django_authopenid.urls')),

)

