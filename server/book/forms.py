from book.models import Author, Book, Language

from django import forms
from django.db.models.query import QuerySet

from django.contrib.admin.widgets import ForeignKeyRawIdWidget
from django.db.models.fields.related import ManyToManyRel
from django.conf import settings
from django.utils.translation import ugettext as _
from django.utils.safestring import mark_safe
from django.utils.encoding import StrAndUnicode, force_unicode
from django.utils.html import escape, conditional_escape
from itertools import chain

from django.forms import Select

class MyManyToManyRawIdWidget(forms.Textarea):
    """
    A Widget for displaying ManyToMany ids in the "raw_id" interface rather than
    in a <select multiple> box.
    """
    def __init__(self, rel, attrs=None):
        self.rel = rel
        super(MyManyToManyRawIdWidget, self).__init__(attrs)

    def render(self, name, value, attrs=None):
        attrs['class'] = 'vManyToManyRawIdAdminField'
        if value:
            value = ',\n'.join([str(Book.objects.get(id=v)) for v in value])
        else:
            value = ''
        if attrs is None:
            attrs = {}
        
        related_url = '../../../%s/%s/' % (self.rel.to._meta.app_label, self.rel.to._meta.object_name.lower())
        params = self.url_parameters()
        if params:
            url = '?' + '&amp;'.join(['%s=%s' % (k, v) for k, v in params.items()])
        else:
            url = ''
        if not attrs.has_key('class'):
            attrs['class'] = 'vForeignKeyRawIdAdminField' # The JavaScript looks for this hook.
        output = [super(MyManyToManyRawIdWidget, self).render(name, value, attrs)]

        output.append('<a href="%s%s" class="related-lookup" id="lookup_id_%s" onclick="return showRelatedObjectLookupPopup(this);"> ' % \
            (related_url, url, name))
        output.append('<img src="%simg/admin/selector-search.gif" width="16" height="16" alt="%s" /></a>' % (settings.ADMIN_MEDIA_PREFIX, _('Lookup')))
        if value:
            output.append(self.label_for_value(value))
        return mark_safe(u''.join(output))

    def url_parameters(self):
        return self.base_url_parameters()

    def label_for_value(self, value):
        return ''

    def base_url_parameters(self):
        params = {}
        if self.rel.limit_choices_to:
            items = []
            for k, v in self.rel.limit_choices_to.items():
                if isinstance(v, list):
                    v = ','.join([str(x) for x in v])
                else:
                    v = str(v)
                items.append((k, v))
            params.update(dict(items))
        return params

    def value_from_datadict(self, data, files, name):
        value = data.get(name, None)
        if value and ',' in value:
            return data[name].split(',')
        if value:
            return [value]
        return None

    def _has_changed(self, initial, data):
        if initial is None:
            initial = []
        if data is None:
            data = []
        if len(initial) != len(data):
            return True
        for pk1, pk2 in zip(initial, data):
            if force_unicode(pk1) != force_unicode(pk2):
                return True
        return False

class LanguageWidget(Select):
    def render_options(self, choices, selected_choices):
        def render_option(option_value, option_label):
            option_value = force_unicode(option_value)
            selected_html = (option_value in selected_choices) and u' selected="selected"' or ''
            return u'<option value="%s"%s>%s</option>' % (
                escape(option_value), selected_html,
                conditional_escape(force_unicode(option_label)))
        # Normalize to strings.
        selected_choices = set([force_unicode(v) for v in selected_choices])
        output = []
        output.append(render_option(322, '[ru] Russian'))
        output.append(render_option(224, '[en] English'))
        output.append(render_option(234, '[fr] French'))

        for option_value, option_label in chain(self.choices, choices):
            if isinstance(option_label, (list, tuple)):
                output.append(u'<optgroup label="%s">' % escape(force_unicode(option_value)))
                for option in option_label:
                    output.append(render_option(*option))
                output.append(u'</optgroup>')
            else:
                output.append(render_option(option_value, option_label))
        return u'\n'.join(output)

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
    book = forms.CharField(widget=MyManyToManyRawIdWidget(rel=ManyToManyRel(to=Book)))

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


