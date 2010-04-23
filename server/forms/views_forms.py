from django import forms
from django.utils.translation import ugettext as _

from forms.autocomplete_widget import AutocompleteWidget

#from views import available_languages

#LANGUAGE_CHOICES = available_languages()

class SampleForm(forms.Form):
    books = forms.CharField(label=_('Books'),
        widget=AutocompleteWidget(choices_url='autocomplete'))

class ExtendedSearch(forms.Form):
    title = forms.CharField(label=_('Title'),
        widget=AutocompleteWidget(choices_url='autocomplete_title', 
                                                related_fields=('author',)))

    author = forms.CharField(label=_('Author'),
        widget=AutocompleteWidget(choices_url='autocomplete_author', 
                                                    related_fields=('title',)))
    
#    language = forms.CharField(widget=forms.Select(choices=LANGUAGE_CHOICES))
