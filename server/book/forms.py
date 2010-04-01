from book.models import Author, Book
from django import forms

class AuthorForm(forms.ModelForm):
    class Meta:
        model = Author


