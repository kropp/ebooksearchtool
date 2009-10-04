from django.db import models

from spec.langcode import LANG_CODE 
# Create your models here.

class Annotation(models.Model):
    name = models.TextField(unique=True)

class BookFile(models.Model):
    link = models.URLField(unique=True)
    size = models.IntegerField()
    type = models.CharField(max_length=10)
    more_info = models.TextField(null = True)
    img_link = models.URLField(null = True)

class Series(models.Model):
    name = models.CharField(max_length=255, unique=True)

class Alias(models.Model):
    name = models.CharField(max_length=255, unique=True)
    name2 = models.CharField(max_length=255, blank=False, null=True)

class Tag(models.Model):
    name = models.CharField(max_length=255, unique=True)

class Book(models.Model):
    title = models.CharField(max_length=255, unique=True)
    lang = models.CharField(max_length=2, choices=LANG_CODE)
    annotation = models.ManyToManyField(Annotation)

