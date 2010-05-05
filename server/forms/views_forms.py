from django import forms
from django.utils.translation import ugettext as _

from forms.autocomplete_widget import AutocompleteWidget

from book.models import Book, Language, Tag
from book.widgets import LanguageWidget

def available_languages():
    '''returns all languages used in books in our data base'''
    return Language.objects.exclude(book__isnull=True)[1:]

class ExtendedSearch(forms.Form):
    title = forms.CharField(label=_('Title'),
        widget=AutocompleteWidget(choices_url='autocomplete_title', 
                                                related_fields=('author',)))

    author = forms.CharField(label=_('Author'),
        widget=AutocompleteWidget(choices_url='autocomplete_author', 
                                                    related_fields=('title',)))
    
    language = forms.CharField(widget=LanguageWidget(choices=available_languages()))
    
    tags = forms.CharField(widget=forms.Select(choices=Tag.objects.all().order_by("name")))
