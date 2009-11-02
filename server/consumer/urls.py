
from django.conf.urls.defaults import *

urlpatterns = patterns(
    'server.consumer.views',
    (r'^$', 'startOpenID'),
    (r'^finish/$', 'finishOpenID'),
    (r'^xrds/$', 'rpXRDS'),
)
