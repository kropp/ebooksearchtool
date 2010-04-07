'''Defines widgets for admin forms '''
# -*- coding: utf-8 -*-

from django import forms
from django.utils.encoding import force_unicode
from django.utils.html import escape, conditional_escape
from itertools import chain
from django.conf import settings
from django.utils.translation import ugettext as _
from django.utils.safestring import mark_safe
from django.contrib.admin.widgets import ManyToManyRawIdWidget

from book.models import Book, Author

class AuthorWidget(forms.Textarea, ManyToManyRawIdWidget):
    """
    A Widget for displaying ManyToMany authors(their names) in the "raw_id" 
    interface rather than in a <select multiple> box.
    """
    def __init__(self, rel, attrs=None):        
        super(AuthorWidget, self).__init__(attrs)
        self.rel = rel

    def render(self, name, value, attrs=None):
        ''' overloads base class method render'''
        attrs['class'] = 'vManyToManyRawIdAdminField'
        if value:
            #########  our view logic 
            value = ',\n'.join([str(Author.objects.get(id=v)) for v in value])
        else:
            value = ''
        if attrs is None:
            attrs = {}
        
        related_url = '../../../%s/%s/' % (self.rel.to._meta.app_label, 
                                        self.rel.to._meta.object_name.lower())
        params = self.url_parameters()
        if params:
            url = '?' + '&amp;'.join(['%s=%s' % (k, v) for k, v in params.items()])
        else:
            url = ''
        if not attrs.has_key('class'):
            attrs['class'] = 'vForeignKeyRawIdAdminField' 
        output = [super(AuthorWidget, self).render(name, value, attrs)]

        output.append('<a href="%s%s" class="related-lookup" id="lookup_id_%s" onclick="return showRelatedObjectLookupPopup(this);"> ' % \
            (related_url, url, name))
        output.append('<img src="%simg/admin/selector-search.gif" width="16" height="16" alt="%s" /></a>' 
                        % (settings.ADMIN_MEDIA_PREFIX, _('Lookup')))
        if value:
            output.append(self.label_for_value(value))
        return mark_safe(u''.join(output))

class LanguageWidget(forms.Select):
    """
    A Widget for displaying english, french, russian language in first place
    """

    def render_options(self, choices, selected_choices):
        ''' overloads base class method render_options'''
        def render_option(option_value, option_label):
            ''' returns 'option' for each language in data base'''
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
                output.append(u'<optgroup label="%s">' 
                                    % escape(force_unicode(option_value)))
                for option in option_label:
                    output.append(render_option(*option))
                output.append(u'</optgroup>')
            else:
                output.append(render_option(option_value, option_label))
        return u'\n'.join(output)
