from django import forms
from django.utils.translation import ugettext as _

from forms.autocomplete_widget import AutocompleteWidget

class SampleForm(forms.Form):
    books = forms.CharField(label=_('Books'),
        widget=AutocompleteWidget(choices_url='autocomplete'))

