from django.conf.urls.defaults import *

from server.views import BookFeed
# Uncomment the next two lines to enable the admin:
# from django.contrib import admin
# admin.autodiscover()

feeds = {
    'allbooks': BookFeed,
}

urlpatterns = patterns('',
(r'^books/search.atom/query\=(allbooks)/$',
'django.contrib.syndication.views.feed',
{'feed_dict' : feeds}
),
)

