try:
    from hashlib import md5
except ImportError:
    from md5 import new as md5

from django.db import models
from django.contrib import admin

from djangosphinx.models import SphinxSearch
from djangosphinx.apis.current import SPH_MATCH_ANY

from spec.langcode import LANG_CODE

NAME_LENGTH = 255
LINK_LENGTH = 4000
TEXT_LENGTH = 10000

AUTHOR_SHOW = (
    (0, 'always'),
    (1, 'admin'),
)

class BookFile(models.Model):
    link = models.TextField(max_length=LINK_LENGTH)
    link_hash = models.CharField(unique=True, max_length=32)
    time_found = models.DateTimeField(auto_now_add=True)
    last_check = models.DateTimeField(auto_now=True)

    size = models.IntegerField(default=0)
    type = models.CharField(max_length=10)
    more_info = models.TextField(max_length=TEXT_LENGTH, null=True, blank=True)
    img_link = models.TextField(max_length=LINK_LENGTH, null=True, blank=True)

    credit = models.IntegerField(default=0)

    def save(self, **kwargs):
        self.link_hash = md5(self.link).hexdigest()
        super(BookFile, self).save(kwargs)

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
    short = models.CharField(max_length=2, unique=True)
    full = models.CharField(max_length=255, unique=True)
    full_national = models.CharField(max_length=255)

    def __unicode__(self):
        return "%s" % ( self.full,)


class Author(models.Model):
    name = models.CharField(max_length=NAME_LENGTH, unique=True, null=False, blank=False)
    #book = models.ManyToManyField(Book, null=True, blank=True)#, limit_choices_to = {'id__lte': 0})
    #book = models.ManyToManyField(Book, null=True, blank=True, through='AuthorBook')#, limit_choices_to = {'id__lte': 0})
    alias = models.ManyToManyField(AuthorAlias, null=True, blank=True)
    tag = models.ManyToManyField(Tag, null=True, blank=True)

    credit = models.IntegerField(default=0)

    #show = models.IntegerField(default=0, choices=AUTHOR_SHOW)

    # for more settings see 'spec/sphinx_conf/001_author_simple.tmplt'
    simple_search = SphinxSearch(
        index='authors_simple',
        weights={
            'name': 100,
        },
        mode='SPH_MATCH_ANY',
    )

    # for more settings see 'spec/sphinx_conf/002_author_soundex.tmplt'
    soundex_search = SphinxSearch(
        index='authors_soundex',
        weights={
            'name': 50,
        },
        mode='SPH_MATCH_ANY',
        morphology='soundex',
        limit=1000,
    )

    def __unicode__(self):
        return '[id %s] %s' % (self.id, self.name)
        
    def existed_books(self):
        return Author.objects.get(id=self.id).book.all()



#class AuthorBook(models.Model):
#    author = models.ForeignKey(Author)
#    book = models.ForeignKey(Book)
#
#    class Meta:
#        unique_together = (("book", "author"),)
#
#    def save(self, **kwargs):
#        super(AuthorBook, self).save(kwargs)
#        self.author.show = 0
#        self.author.save()
#
#    def delete(self):
#        print 'delete'
#        super(AuthorBook, self).delete()
#        if not self.author.book.count():
#            self.author.show = 1
#            self.author.save()



class Book(models.Model):
    title = models.CharField(max_length=NAME_LENGTH)
    lang = models.CharField(max_length=2, choices=LANG_CODE)
    language = models.ForeignKey(Language)
    book_file = models.ManyToManyField(BookFile)
    series = models.ManyToManyField(Series, null=True, blank=True)
    tag = models.ManyToManyField(Tag, null=True, blank=True)

    credit = models.IntegerField(default=0)

    author = models.ManyToManyField(Author, null=True, blank=True)

    # for more settings see 'spec/sphinx_conf/003_book_title.tmplt'
    title_search = SphinxSearch(
        index='book_title',
        weights={
            'title': 100,
        },
        mode='SPH_MATCH_ANY'
    )

    def book_authors(self):
        return Book.objects.get(id=self.id).author.all()

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


#class Annotation2(models.Model):
#    name = models.TextField()
#    book = models.ForeignKey(Book)
#
#    def __unicode__(self):
#        return '[id %s] %s' % (self.id, self.name)

class Annotation(models.Model):
    name = models.TextField()
    book = models.ForeignKey(Book)

    def __unicode__(self):
        return '[id %s] %s' % (self.id, self.name[0:20]+"...")
