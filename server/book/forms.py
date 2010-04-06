from book.models import Author, Book, Language

from django import forms
from django.db.models.query import QuerySet

from django.contrib.admin.widgets import ForeignKeyRawIdWidget
from django.db.models.fields.related import ManyToManyRel
from django.conf import settings
from django.utils.translation import ugettext as _
from django.utils.safestring import mark_safe

from django.forms import Select

from widgets import *

class BookForm(forms.ModelForm):
    class Meta:
#        exclude=('tag', 'book')
        model = Book

    CREDIT_CHOICES = (
        ('0', 'NOT CHECKED'),
        ('1', 'CHECKING'),
        ('2', 'CHECKED'),
    )


    language = forms.ModelChoiceField(queryset=Language.objects.all(), widget=LanguageWidget())
    credit = forms.IntegerField(widget=forms.Select(choices=CREDIT_CHOICES))


class AuthorForm(forms.ModelForm):
    class Meta:
#        exclude=('tag', 'book')
        model = Author
    book = forms.CharField(widget=BookWidget(rel=ManyToManyRel(to=Book)))
    CREDIT_CHOICES = (
        ('0', 'NOT CHECKED'),
        ('1', 'CHECKING'),
        ('2', 'CHECKED'),
    )

    credit = forms.IntegerField(widget=forms.Select(choices=CREDIT_CHOICES))

    def save(self, commit=True):
        """
        Saves this ``form``'s cleaned_data into model instance
        ``self.instance``.

        If commit=True, then the changes to ``instance`` will be saved to the
        database. Returns ``instance``.
        """
        if self.instance.pk is None:
            fail_message = 'created'
        else:
            fail_message = 'changed'
        return save_author(self, self.instance, self._meta.fields,
                             fail_message, commit, exclude=self._meta.exclude)


def save_author(form, instance, fields=None, fail_message='saved',
                  commit=True, exclude=None):
    """
    Saves bound Form ``form``'s cleaned_data into model instance ``instance``.

    If commit=True, then the changes to ``instance`` will be saved to the
    database. Returns ``instance``.
    """
    from django.db import models
    opts = instance._meta
    if form.errors:
        raise ValueError("The %s could not be %s because the data didn't"
                         " validate." % (opts.object_name, fail_message))
    cleaned_data = form.cleaned_data

    ############### my logic for saving
    book_list = cleaned_data.pop('book')[3:-2].split(',')

    books = list()
    for i in book_list:
        books.append(i[i.find('[')+4:i.find(']')])

    cleaned_data['book'] = books

    ################

    file_field_list = []
    for f in opts.fields:
        if not f.editable or isinstance(f, models.AutoField) \
                or not f.name in cleaned_data:
            continue
        if fields and f.name not in fields:
            continue
        if exclude and f.name in exclude:
            continue
        # OneToOneField doesn't allow assignment of None. Guard against that
        # instead of allowing it and throwing an error.
        if isinstance(f, models.OneToOneField) and cleaned_data[f.name] is None:
            continue
        # Defer saving file-type fields until after the other fields, so a
        # callable upload_to can use the values from other fields.
        if isinstance(f, models.FileField):
            file_field_list.append(f)
        else:
            f.save_form_data(instance, cleaned_data[f.name])

    for f in file_field_list:
        f.save_form_data(instance, cleaned_data[f.name])

    # Wrap up the saving of m2m data as a function.
    def save_m2m():
        opts = instance._meta
        cleaned_data = form.cleaned_data
        for f in opts.many_to_many:
            if fields and f.name not in fields:
                continue
            if f.name in cleaned_data:
                f.save_form_data(instance, cleaned_data[f.name])
    if commit:
        # If we are committing, save the instance and the m2m data immediately.
        instance.save()
        save_m2m()
    else:
        # We're not committing. Add a method to the form to allow deferred
        # saving of m2m data.
        form.save_m2m = save_m2m
    return instance


