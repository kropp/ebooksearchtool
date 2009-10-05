from django.conf.urls.defaults import *

from server.views import BookFeed

feeds = {
    'allbooks': BookFeed,
}

from server.views import my_test
urlpatterns = patterns('',
(r'^tipa/$', my_test),
(r'^tipa/add/([^/]+)/$', my_test),
(r'^books/search.atom/query\=(allbooks)/$',
'django.contrib.syndication.views.feed',
{'feed_dict' : feeds}
),
)

