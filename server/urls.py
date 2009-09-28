from django.conf.urls.defaults import *

from server.views import LatestEntries
# Uncomment the next two lines to enable the admin:
# from django.contrib import admin
# admin.autodiscover()
feeds = {
    'latest': LatestEntries,
}

urlpatterns = patterns('',
(r'^feeds/(?P<url>.*)/$',
 'django.contrib.syndication.views.feed',
 {'feed_dict': feeds}
),
)
