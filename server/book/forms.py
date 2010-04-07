'''Defines forms for admin view'''
# -*- coding: utf-8 -*-

from book.models import Author, Book, Language

from django import forms

from django.db.models.fields.related import ManyToManyRel

from book.widgets import AuthorWidget, LanguageWidget

class BookForm(forms.ModelForm):
    '''form for represent model.Book in admin change veiw'''
    class Meta:
        '''defines model for this form '''
        model = Book

    CREDIT_CHOICES = (
        ('0', 'NOT CHECKED'),
        ('1', 'CHECKING'),
        ('2', 'CHECKED'),
    )

    author = forms.CharField(widget=AuthorWidget(rel=ManyToManyRel(to=Author)))
    language = forms.ModelChoiceField(queryset=Language.objects.all(), 
                                        widget=LanguageWidget())
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
        return save_book(self, self.instance, self._meta.fields,
                             fail_message, commit, exclude=self._meta.exclude)


class AuthorForm(forms.ModelForm):
    '''form for represent model.Author in admin change veiw'''
    class Meta:
        '''defines model for this form '''
        model = Author
#    book = forms.CharField(widget=BookWidget(rel=ManyToManyRel(to=Book)))
    CREDIT_CHOICES = (
        ('0', 'NOT CHECKED'),
        ('1', 'CHECKING'),
        ('2', 'CHECKED'),
    )

    credit = forms.IntegerField(widget=forms.Select(choices=CREDIT_CHOICES))


def save_book(form, instance, fields=None, fail_message='saved',
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
    authors_list = cleaned_data.pop('author')[3:-2].split(',')

    authors = list()
    for i in authors_list:
        authors.append(i[i.find('[')+4:i.find(']')])

    cleaned_data['author'] = authors

    ################

    file_field_list = []
    for field in opts.fields:
        if not field.editable or isinstance(field, models.AutoField) \
                or not field.name in cleaned_data:
            continue
        if fields and field.name not in fields:
            continue
        if exclude and field.name in exclude:
            continue
        # OneToOneField doesn't allow assignment of None. Guard against that
        # instead of allowing it and throwing an error.
        if isinstance(field, models.OneToOneField) and cleaned_data[field.name] is None:
            continue
        # Defer saving file-type fields until after the other fields, so a
        # callable upload_to can use the values from other fields.
        if isinstance(field, models.FileField):
            file_field_list.append(field)
        else:
            field.save_form_data(instance, cleaned_data[field.name])

    for field in file_field_list:
        field.save_form_data(instance, cleaned_data[field.name])

    # Wrap up the saving of m2m data as a function.
    def save_m2m():
        ''' defines how to save object's m2m fields'''
        opts = instance._meta
        cleaned_data = form.cleaned_data
        for field in opts.many_to_many:
            if fields and field.name not in fields:
                continue
            if field.name in cleaned_data:
                field.save_form_data(instance, cleaned_data[field.name])
    if commit:
        # If we are committing, save the instance and the m2m data immediately.
        instance.save()
        save_m2m()
    else:
        # We're not committing. Add a method to the form to allow deferred
        # saving of m2m data.
        form.save_m2m = save_m2m
    return instance


