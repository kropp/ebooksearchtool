''' Defines admin view for our model '''
# -*- coding: utf-8 -*-

import book.admin_view

from django.contrib import admin

from django import template
from django.shortcuts import render_to_response

from django.contrib.admin.options import IncorrectLookupParameters
from django.utils.translation import ugettext, ungettext
from django.utils.encoding import force_unicode
from django.core.exceptions import PermissionDenied
from django.http import HttpResponseRedirect

from book.models import Book, Author, Tag, Annotation, BookFile
from book.forms import BookForm, AuthorForm

class AuthorAdmin(admin.ModelAdmin):
    ''' Admin view for model.Author'''
    form = AuthorForm
    search_fields = ('name', )
    filter_horizontal = ('tag',)
#    filter_horizontal = ('tag', 'book')
    list_display = ('name', 'credit', 'id')
    list_filter = ('credit',)
    raw_id_fields = ('book',)
#    list_per_page = 10

    fields = ('name', 'tag', 'credit', )

class BookAdmin(admin.ModelAdmin):
    ''' Admin view for model.Book'''
    form = BookForm

    filter_horizontal = ('tag',)
    fields = ('title', 'language', 'credit', 'tag', 'book_file')

    list_per_page = 10
    search_fields = ('title', 'id')
    list_display = ('title', 'language', 'credit', 'id' )
    list_filter = ('credit',)
    
    def changelist_view(self, request, extra_context=None):
        "The 'change list' admin view for this model."
        from django.contrib.admin.views.main import ChangeList, ERROR_FLAG
        opts = self.model._meta
        app_label = opts.app_label
        if not self.has_change_permission(request, None):
            raise PermissionDenied

        # Check actions to see if any are available on this changelist
        actions = self.get_actions(request)

        # Remove action checkboxes if there aren't any actions available.
        list_display = list(self.list_display)
        if not actions:
            try:
                list_display.remove('action_checkbox')
            except ValueError:
                pass

        try:
            change_list = ChangeList(request, self.model, list_display, 
                self.list_display_links, self.list_filter, self.date_hierarchy, 
                self.search_fields, self.list_select_related, self.list_per_page, 
                self.list_editable, self)
        except IncorrectLookupParameters:
            if ERROR_FLAG in request.GET.keys():
                return render_to_response('admin/invalid_setup.html', {'title': 
                                        ugettext('Database error')})
            return HttpResponseRedirect(request.path + '?' + ERROR_FLAG + '=1')

        if actions and request.method == 'POST':
            response = self.response_action(request, queryset=change_list.get_query_set())
            if response:
                return response

        formset = change_list.formset = None

        if request.method == "POST" and self.list_editable:
            FormSet = self.get_changelist_formset(request)
            formset = change_list.formset = FormSet(request.POST, request.FILES, 
                                            queryset=change_list.result_list)
            if formset.is_valid():
                changecount = 0
                for form in formset.forms:
                    if form.has_changed():
                        obj = self.save_form(request, form, change=True)
                        self.save_model(request, obj, form, change=True)
                        form.save_m2m()
                        change_msg = self.construct_change_message(request, form, 
                                                                   None)
                        self.log_change(request, obj, change_msg)
                        changecount += 1

                if changecount:
                    if changecount == 1:
                        name = force_unicode(opts.verbose_name)
                    else:
                        name = force_unicode(opts.verbose_name_plural)
                    msg = ungettext("%(count)s %(name)s was changed successfully.",
                                    "%(count)s %(name)s were changed successfully.",
                                    changecount) % {'count': changecount,
                                                    'name': name,
                                                    'obj': force_unicode(obj)}
                    self.message_user(request, msg)

                return HttpResponseRedirect(request.get_full_path())

        elif self.list_editable:
            FormSet = self.get_changelist_formset(request)
            formset = change_list.formset = FormSet(queryset=change_list.result_list)

        if formset:
            media = self.media + formset.media
        else:
            media = self.media

        if actions:
            action_form = self.action_form(auto_id=None)
            action_form.fields['action'].choices = self.get_action_choices(request)
        else:
            action_form = None

        context = {
            'title': change_list.title,
            'is_popup': change_list.is_popup,
            'cl': change_list,
            'media': media,
            'has_add_permission': self.has_add_permission(request),
            'root_path': self.admin_site.root_path,
            'app_label': app_label,
            'action_form': action_form,
            'actions_on_top': self.actions_on_top,
            'actions_on_bottom': self.actions_on_bottom,
        }
        context.update(extra_context or {})
        context_instance = template.RequestContext(request, 
                                               current_app=self.admin_site.name)
        return render_to_response(self.change_list_template or [
            'admin/%s/%s/change_list.html' % (app_label, opts.object_name.lower()),
            'admin/%s/change_list.html' % app_label,
            'admin/change_list.html'
        ], context, context_instance=context_instance)

class AnnotationAdmin(admin.ModelAdmin):
    ''' Admin view for model.Annotation'''
    search_fields = ('name', )
    list_per_page = 10

class TagAdmin(admin.ModelAdmin):
    ''' Admin view for model.Tag'''
    search_fields = ('name', )
    list_per_page = 10

class BookFileAdmin(admin.ModelAdmin):
    ''' Admin view for model.BookFile'''
    search_fields = ('type', 'link', 'id')
    list_display = ('link', 'type', 'credit', 'id',)
    list_filter = ('last_check', 'credit', 'type')
    fields = ('link', 'type', 'credit', 'size', 'more_info', 'img_link')
    list_per_page = 10

admin.site.register(Book, BookAdmin)

admin.site.register(Author, AuthorAdmin)

admin.site.register(Tag, TagAdmin)

admin.site.register(Annotation, AnnotationAdmin)

admin.site.register(BookFile, BookFileAdmin)

#admin.site.register(Series)

#admin.site.register(AuthorAlias)


