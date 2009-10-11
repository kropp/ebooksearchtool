from django.conf.urls.defaults import *
from server.views import BookFeed
from server.book.views import data_modify 
from server.book.action_handler import ACTION, TARGET

feeds = {
    'allbooks': BookFeed,
}

from server.views import my_test

urlpatterns = patterns('',
  (r'^tipa/$', my_test),
  (r'^tipa/add/([^/]+)/$', my_test),

  # interface for analizer/crawler
  (r'^data/\+author/$', data_modify, {'action': ACTION['insert'], 'target': TARGET['author']}),
  (r'^data/\+book/$', data_modify, {'action': ACTION['insert'], 'target': TARGET['book']}),

  (r'^books/search.atom/query\=(allbooks)$',
    'django.contrib.syndication.views.feed',
    {'feed_dict' : feeds}
  ),
)

