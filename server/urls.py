from django.conf.urls.defaults import *

from server.book.views import data_modify 
from server.book.action_handler import ACTION


urlpatterns = patterns('',

    # interface for analizer/crawler
    (r'^data/get/?$', data_modify, {'action': ACTION['get'],}),
    (r'^data/insert/?$', data_modify, {'action': ACTION['insert'],}),

    # interface for search
    (r'^books/search.atom/?$', 'server.views.search_request_to_server'),
    (r'^books/search.atom/?$', 'server.views.search_request_to_server'),
    (r'^book.atom/id(\d{1,})/?$', 'server.views.book_request_to_server'),
)

