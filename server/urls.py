from django.conf.urls.defaults import *
from server.views import BookFeed
from server.book.views import data_modify 
from server.book.action_handler import ACTION

feeds = {
    'allbooks': BookFeed,
}

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
#    (r'^books/search.atom/query\=(?P<url>allbooks)/$',
#      'django.contrib.syndication.views.feed',
#      {'feed_dict' : feeds}
#    ),
    
    (r'^books/search.atom/?$', 'server.views.search_request_to_server'),
)

