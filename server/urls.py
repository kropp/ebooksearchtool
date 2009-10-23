from django.conf.urls.defaults import *
from server.book.views import data_modify 
from server.book.action_handler import ACTION

def test_f(request):
    print request.GET['query']
    print 'test_f'
    response = 'sd'
    return response


from server.views import my_test

urlpatterns = patterns('',
    (r'^tipa/$', my_test),
    (r'^tipa/add/([^/]+)/$', my_test),

    # interface for analizer/crawler
    (r'^data/get/?$', data_modify, {'action': ACTION['get'],}),
    (r'^data/insert/?$', data_modify, {'action': ACTION['insert'],}),

    # interface for search
   
    (r'^books/search.atom/?$', 'server.views.search_request_to_server'),
    (r'^books/search.atom/?$', 'server.views.search_request_to_server'),
    (r'^book.atom/id([\d]{1,})/?$', 'server.views.book_request_to_server'),
)



