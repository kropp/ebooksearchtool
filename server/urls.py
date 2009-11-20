"""adds ebooks prefix to urls"""
from django.conf.urls.defaults import include
from django.conf.urls.defaults import patterns

from django.contrib import admin

admin.autodiscover()

urlpatterns = patterns('',
    (r'^ebooks/', include('urls_root')),
)

