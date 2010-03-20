from django.db import models
from django.contrib import admin

from djangosphinx.models import SphinxSearch
from djangosphinx.apis.current import SPH_MATCH_ANY

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

    credit = models.IntegerField(default=0)

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

    credit = models.IntegerField(default=0)

    # for more settings see 'spec/sphinx_conf/003_book_title.tmplt'
    title_search = SphinxSearch(
        index='book_title',
        weights={
            'title': 100,
        },
        mode='SPH_MATCH_ANY'
    )

#    # for more settings see 'spec/sphinx_conf/004_book_title_author.tmplt'
#    title_author_search = SphinxSearch(
#        index='book_title_author',
#        weights={
#            'author_name': 100,
#        },
#        mode='SPH_MATCH_ANY',
#    )

#    # for more settings see 'spec/sphinx_conf/004_book_title_annotation.tmplt'
#    title_annotation_search = SphinxSearch(
#        index='book_title_annotation',
#        weights={
#            'title': 100,
#        }
#    )

    def __unicode__(self):
        return '[id %s] %s (%s)' % (self.id, self.title, self.lang)


class Author(models.Model):
    name = models.CharField(max_length=NAME_LENGTH, unique=True)
    book = models.ManyToManyField(Book)
    alias = models.ManyToManyField(AuthorAlias)
    tag = models.ManyToManyField(Tag)

    credit = models.IntegerField(default=0)

    # for more settings see 'spec/sphinx_conf/001_author_simple.tmplt'
    simple_search = SphinxSearch(
        index='authors_simple',
        weights={
            'name': 100,
        },
    )

    # for more settings see 'spec/sphinx_conf/002_author_soundex.tmplt'
    soundex_search = SphinxSearch(
        index='authors_soundex',
        weights={
            'name': 50,
        },
        mode='SPH_MATCH_ANY'
    )

    def __unicode__(self):
        return "%s" % (self.name)
        

