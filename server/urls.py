from django.conf.urls.defaults import *

from server.book.views import data_modify 
from server.book.action_handler import ACTION

from django.contrib import admin

admin.autodiscover()

urlpatterns = patterns('',

    # interface for analizer/crawler
    (r'^data/get/?$', data_modify, {'action': ACTION['get'],}),
    (r'^data/insert/?$', data_modify, {'action': ACTION['insert'],}),

    # interface for search
    (r'^books/search.atom/?$', 'server.views.search_request_to_server'),
    (r'^book.atom/id(\d{1,})/?$', 'server.views.book_request_to_server'),
    (r'^author.atom/id(\d{1,})/?$', 'server.views.author_request_to_server'),
    
    #admin
    (r'^admin/(.*)', admin.site.root),
    (r'^admin/doc/', include('django.contrib.admindocs.urls')),
    
    #opensearchdescription
    (r'^opensearch/?$', 'server.views.opensearch_description'),
    
    #user_information
    (r'^user/insert/?$', 'server.reader.views.insert_user_information'),
)

