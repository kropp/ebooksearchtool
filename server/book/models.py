from django.db import models
from django.contrib import admin

from djangosphinx.models import SphinxSearch

from spec.langcode import LANG_CODE

NAME_LENGTH = 255
LINK_LENGTH = 4000
TEXT_LENGTH = 10000

class Annotation(models.Model):
    name = models.TextField(max_length=TEXT_LENGTH)

    def __unicode__(self):
        return self.name


class BookFile(models.Model):
    link = models.TextField(max_length=LINK_LENGTH)
    link_hash = models.CharField(unique=True, max_length=32)
    time_found = models.DateTimeField(auto_now_add=True)
    last_check = models.DateTimeField(auto_now=True)

    size = models.IntegerField(default=0)
    type = models.CharField(max_length=10)
    more_info = models.TextField(max_length=TEXT_LENGTH, null=True)
    img_link = models.TextField(null=True, max_length=LINK_LENGTH)

    def __unicode__(self):
        return '%s [size %s] [type %s]' % (self.link, self.size, self.type)


class Series(models.Model):
    name = models.CharField(max_length=NAME_LENGTH, unique=True)

    def __unicode__(self):
        return self.name


class AuthorAlias(models.Model):
    name = models.CharField(max_length=NAME_LENGTH, unique=True)

    def __unicode__(self):
        return self.name


class Tag(models.Model):
    name = models.CharField(max_length=NAME_LENGTH, unique=True)

    def __unicode__(self):
        return self.name


class Language(models.Model):
    short = models.CharField(max_length=2)
    full = models.CharField(max_length=255, unique=True)
    full_national = models.CharField(max_length=255)

    def __unicode__(self):
        return "[%s] %s" % (self.short, self.full)


class Book(models.Model):
    title = models.CharField(max_length=NAME_LENGTH)
    lang = models.CharField(max_length=2, choices=LANG_CODE)
    language = models.ForeignKey(Language)
    annotation = models.ManyToManyField(Annotation)
    book_file = models.ManyToManyField(BookFile)
    series = models.ManyToManyField(Series)
    tag = models.ManyToManyField(Tag)

    title_search = SphinxSearch(
        index='book_simple',
        weights={
            'title': 100,
        },
    )

    title_annotation_search = SphinxSearch(
        index='book_title_annotation',
        weights={
            'title': 100,
            'annotation.name': 50,
        }
    )

    def __unicode__(self):
        return '[id %s] %s (%s)' % (self.id, self.title, self.lang)


#class AuthorSearchManager(object):
#    def query(self, query):
#        from spec.search_util import soundex_for_string, join_query_list
#
#        simple_result = Author.simple_search.query(query)
#        soundex_result = Author.soundex_search.query(query)
#
#        return join_query_list(simple_result, soundex_result)


class Author(models.Model):
    name = models.CharField(max_length=NAME_LENGTH, unique=True)
    book = models.ManyToManyField(Book)
    alias = models.ManyToManyField(AuthorAlias)
    tag = models.ManyToManyField(Tag)

    simple_search = SphinxSearch(
        index='authors_simple',
        weights={
            'name': 100,
        },
    )

    soundex_search = SphinxSearch(
        index='authors_soundex',
        weights={
            'name': 50,
        },
    )

#    search = AuthorSearchManager()

    def __unicode__(self):
        return "%s" % (self.name)
        

