from django.conf.urls.defaults import *

from server.book.views import data_modify 
from server.book.action_handler import ACTION

from django.contrib import admin

from django.conf.urls.defaults import *

import views


admin.autodiscover()

urlpatterns = patterns('',
    (r'^ebooks/', include('urls_root')),
)

