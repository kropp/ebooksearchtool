''' Defines admin view for our model '''
# -*- coding: utf-8 -*-

import book.templatetags.book_tags

from django.contrib import admin

from django import template
from django.shortcuts import render_to_response

from django.contrib.admin.options import IncorrectLookupParameters
from django.utils.translation import ugettext, ungettext
from django.utils.encoding import force_unicode
from django.core.exceptions import PermissionDenied
from django.http import HttpResponseRedirect

from book.models import Book, Author, Tag, Annotation, BookFile
from book.forms import BookForm, AuthorForm, BookFileForm

class AuthorAdmin(admin.ModelAdmin):
    ''' Admin view for model.Author'''
    form = AuthorForm

    search_fields = ('name', )
    list_display = ('name', 'credit', 'id')
    list_filter = ('credit',)

    fields = ('name', 'tag', 'credit', )
    filter_horizontal = ('tag',)

    list_per_page = 10

class BookAdmin(admin.ModelAdmin):
    ''' Admin view for model.Book'''
    form = BookForm

    filter_horizontal = ('tag',)
    fields = ('title', 'author', 'language', 'credit', 'tag')
    raw_id_fields = ('author', 'annotation')

    search_fields = ('title', 'id')
    list_display = ('title', 'language', 'credit', 'id' )
    list_filter = ('credit',)

    list_per_page = 10

class AnnotationAdmin(admin.ModelAdmin):
    ''' Admin view for model.Annotation'''
    search_fields = ('name', )
    list_per_page = 10

class TagAdmin(admin.ModelAdmin):
    ''' Admin view for model.Tag'''
    search_fields = ('name', )
    list_per_page = 10

class BookFileAdmin(admin.ModelAdmin):
    ''' Admin view for model.BookFile'''
    form = BookFileForm

    search_fields = ('type', 'link', 'id')
    list_display = ('link', 'type', 'credit', 'id',)
    list_filter = ('last_check', 'credit', 'type')
    fields = ('link', 'type', 'img_link', 'credit', 'more_info', )
    list_per_page = 10


admin.site.register(Book, BookAdmin)

admin.site.register(Author, AuthorAdmin)

admin.site.register(Tag, TagAdmin)

admin.site.register(Annotation, AnnotationAdmin)

admin.site.register(BookFile, BookFileAdmin)

#admin.site.register(Series)

#admin.site.register(AuthorAlias)


