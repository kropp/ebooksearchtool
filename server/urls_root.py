from django.conf.urls.defaults import include
from django.conf.urls.defaults import patterns
from django.contrib import admin

from book.views import who, analyzer_view

import views


urlpatterns = patterns('',
    #home
    (r'^$', 'views.catalog',
        {'response_type': 'xhtml',}),

    # interface for analizer/crawler
#    (r'^data/get/?$', data_modify, {'action': ACTION['get'],}),
#    (r'^data/insert/?$', data_modify, {'action': ACTION['insert'],}),
    (r'^data/?$', who),
    # new interface for analyzer
    (r'^data/search/?$', analyzer_view, {'action': 'SEARCH'},),
    (r'^data/modify/?$', analyzer_view, {'action': 'MODIFY'},),


    # interface for search
    (r'^search.atom/?$', 'search.search_request_to_server',
        {'response_type': 'atom', 'is_all': 'no'}),
    (r'^search/?$', 'search.search_request_to_server',
        {'response_type': 'xhtml', 'is_all': 'no'}),
        
    #all books
    (r'^all.atom/?$', 'search.search_request_to_server',
        {'response_type': 'atom', 'is_all': 'yes'}),
    (r'^all/?$', 'search.search_request_to_server',
        {'response_type': 'xhtml', 'is_all': 'yes'}),
    
    # requests
    (r'^book.atom/id(\d{1,})/?$', 'views.book_request',
        {'response_type': 'atom',}),
    (r'^book/id(\d{1,})/?$', 'views.book_request',
        {'response_type': 'xhtml',}),
    
    (r'^author.atom/id(\d{1,})/?$', 'views.author_request',
        {'response_type': 'atom',}),
    (r'^author/id(\d{1,})/?$', 'views.author_request',
        {'response_type': 'xhtml',}),
    
    #book catalog
    (r'^catalog.atom/?$', 'views.catalog',
        {'response_type': 'atom',}),
    (r'^catalog/?$', 'views.catalog',
        {'response_type': 'xhtml',}),
    
    #books sorted by authors, languages, subjects
    (r'^discover/authors.atom/?$', 'views.books_by_authors',
        {'response_type': 'atom',}),
    (r'^discover/authors/?$', 'views.books_by_authors',
        {'response_type': 'xhtml',}),
        
    (r'^discover/languages.atom/?$', 'views.books_by_language',
        {'response_type': 'atom',}),
    (r'^discover/languages/?$', 'views.books_by_language',
        {'response_type': 'xhtml',}),
        
    (r'^discover/subjects.atom/?$', 'views.books_by_tags',
        {'response_type': 'atom',}),
    (r'^discover/subjects/?$', 'views.books_by_tags',
        {'response_type': 'xhtml',}),
        
    (r'^simple_search/?$', 'views.simple_search'),
    (r'^extended_search/?$', 'views.extended_search'),
    
    # no book cover available
    (r'^pic/nobookcover/?$', 'views.no_book_cover'),
    
    #admin
#    (r'^admin/book/author/(.*)/$', 'book.templatetags.book_tags.author'),      #my admin view for author

    (r'^admin/(.*)', admin.site.root),
#    (r'^admin/doc/', include('django.contrib.admindocs.urls')),
    
    #opensearchdescription
    (r'^opensearch/?$', 'views.opensearch_description'),
    
    #user_information
#    (r'^user/insert/?$', 'reader.views.insert_user_information'),
    
    #openid
#    (r'^$', 'django.views.generic.simple.direct_to_template',
#        {'template': 'home.html'}),
    (r'^account/', include('django_authopenid.urls')),

    #media 
    (r'^site_media/(?P<path>.*)$', 'django.views.static.serve',
        {'document_root': 'media'}),


)

