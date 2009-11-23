from django.conf.urls.defaults import include
from django.conf.urls.defaults import patterns

from book.views import data_modify 
from book.action_handler import ACTION

from django.contrib import admin

import views


urlpatterns = patterns('',

    # interface for analizer/crawler
    (r'^data/get/?$', data_modify, {'action': ACTION['get'],}),
    (r'^data/insert/?$', data_modify, {'action': ACTION['insert'],}),

    # interface for search
    (r'^search.atom/?$', 'views.search_request_to_server', {'response_type': 'atom',}),
    (r'^search/?$', 'views.search_request_to_server', {'response_type': 'xhtml',}),
    
    (r'^book.atom/id(\d{1,})/?$', 'views.book_request_to_server', {'response_type': 'atom',}),
    (r'^book/id(\d{1,})/?$', 'views.book_request_to_server', {'response_type': 'xhtml',}),
    
    (r'^author.atom/id(\d{1,})/?$', 'views.author_request_to_server', {'response_type': 'atom',}),
    (r'^author/id(\d{1,})/?$', 'views.author_request_to_server', {'response_type': 'xhtml',}),
    
    #all books
    (r'^all.atom/?$', 'views.all_books_request_to_server', {'response_type': 'atom',}),
    (r'^all/?$', 'views.all_books_request_to_server', {'response_type': 'xhtml',}),
    
    #admin
    (r'^admin/(.*)', admin.site.root),
#    (r'^admin/doc/', include('django.contrib.admindocs.urls')),
    
    #opensearchdescription
    (r'^opensearch/?$', 'server.views.opensearch_description'),
    
    #user_information
#    (r'^user/insert/?$', 'reader.views.insert_user_information'),
    
    #openid
    (r'^$', 'django.views.generic.simple.direct_to_template', {'template': 'home.html'}),
    (r'^account/', include('django_authopenid.urls')),

    
)

