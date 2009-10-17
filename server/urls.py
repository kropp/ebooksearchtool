from django.conf.urls.defaults import *
from server.views import BookFeed
from server.book.views import data_modify 
from server.book.action_handler import ACTION

feeds = {
    'allbooks': BookFeed,
}

from server.views import my_test

urlpatterns = patterns('',
    (r'^tipa/$', my_test),
    (r'^tipa/add/([^/]+)/$', my_test),
  
    # interface for analizer/crawler
    (r'^data/get/$', data_modify, {'action': ACTION['get'],}),
    (r'^data/insert/$', data_modify, {'action': ACTION['insert'],}),
  
    (r'^books/search.atom/query\=(allbooks)$',
      'django.contrib.syndication.views.feed',
      {'feed_dict' : feeds}
    ),
)

