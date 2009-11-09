from django.db import models

from spec.langcode import LANG_CODE 

from django.contrib import admin

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


class Alias(models.Model):
    name = models.CharField(max_length=NAME_LENGTH, unique=True)

    def __unicode__(self):
        return self.name


class Tag(models.Model):
    name = models.CharField(max_length=NAME_LENGTH, unique=True)

    def __unicode__(self):
        return self.name


class Book(models.Model):
    title = models.CharField(max_length=NAME_LENGTH)
    lang = models.CharField(max_length=2, choices=LANG_CODE)
    annotation = models.ManyToManyField(Annotation)
    book_file = models.ManyToManyField(BookFile)
    series = models.ManyToManyField(Series)
    tag = models.ManyToManyField(Tag)

    def __unicode__(self):
        return '[id %s] %s (%s)' % (self.id, self.title, self.lang)


class Author(models.Model):
    name = models.CharField(max_length=NAME_LENGTH, unique=True)
    book = models.ManyToManyField(Book)
    alias = models.ManyToManyField(Alias)
    tag = models.ManyToManyField(Tag)

    def __unicode__(self):
        return self.name
        

