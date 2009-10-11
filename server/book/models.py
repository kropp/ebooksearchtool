from django.db import models

from spec.langcode import LANG_CODE 

LINK_LENGTH = 255

class Annotation(models.Model):
    name = models.TextField(max_length=10000)

class BookFile(models.Model):
    link = models.URLField(unique=True, max_length=LINK_LENGTH)
    size = models.IntegerField()
    type = models.CharField(max_length=10)
    more_info = models.TextField(max_length=10000, null = True)
    img_link = models.URLField(null = True, max_length=LINK_LENGTH)

class Series(models.Model):
    name = models.CharField(max_length=255, unique=True)

class Alias(models.Model):
    name = models.CharField(max_length=255, unique=True)

class Tag(models.Model):
    name = models.CharField(max_length=255, unique=True)

class Book(models.Model):
    title = models.CharField(max_length=255)
    lang = models.CharField(max_length=2, choices=LANG_CODE)
    annotation = models.ManyToManyField(Annotation)
    book_file = models.ManyToManyField(BookFile)
    series = models.ManyToManyField(Series)
    tag = models.ManyToManyField(Tag)

class Author(models.Model):
    name = models.CharField(max_length=255, unique=True)
    book = models.ManyToManyField(Book)
    alias = models.ManyToManyField(Alias)
    tag = models.ManyToManyField(Tag)
