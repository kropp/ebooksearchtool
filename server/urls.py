from django.conf.urls.defaults import *

from server.book.views import data_modify 
from server.book.action_handler import ACTION

from django.contrib import admin

from django.conf.urls.defaults import *

import views


admin.autodiscover()

urlpatterns = patterns('',

    # interface for analizer/crawler
    (r'^data/get/?$', data_modify, {'action': ACTION['get'],}),
    (r'^data/insert/?$', data_modify, {'action': ACTION['insert'],}),

    # interface for search
    (r'^books/search.atom/?$', 'server.views.search_request_to_server', {'type': 'atom',}),
    (r'^books/search/?$', 'server.views.search_request_to_server', {'type': 'xhtml',}),
    
    (r'^book.atom/id(\d{1,})/?$', 'server.views.book_request_to_server', {'type': 'atom',}),
    (r'^book/id(\d{1,})/?$', 'server.views.book_request_to_server', {'type': 'xhtml',}),
    
    (r'^author.atom/id(\d{1,})/?$', 'server.views.author_request_to_server', {'type': 'atom',}),
    (r'^author/id(\d{1,})/?$', 'server.views.author_request_to_server', {'type': 'xhtml',}),
    
    #admin
    (r'^admin/(.*)', admin.site.root),
#    (r'^admin/doc/', include('django.contrib.admindocs.urls')),
    
    #opensearchdescription
    (r'^opensearch/?$', 'server.views.opensearch_description'),
    
    #user_information
#    (r'^user/insert/?$', 'server.reader.views.insert_user_information'),
    
    #openid
    (r'^$', 'django.views.generic.simple.direct_to_template', {'template': 'home.html'}),
    (r'^account/', include('django_authopenid.urls')),

    
)

