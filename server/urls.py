"""adds ebooks prefix to urls"""
from django.conf.urls.defaults import include
from django.conf.urls.defaults import patterns

from django.contrib import admin

try:
    from settings import ROOT_URL
except ImportError:
    ROOT_URL = r''

admin.autodiscover()

urlpatterns = patterns('',
    (ROOT_URL, include('urls_root')),
)


