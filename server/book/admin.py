from book.models import *
from book.forms import *

class AuthorAdmin(admin.ModelAdmin):
    form = AuthorForm
    search_fields = ('name', )
    filter_horizontal = ('tag',)
#    filter_horizontal = ('tag', 'book')
    list_display = ('name', 'credit', 'id')
    list_filter = ('credit',)
    raw_id_fields = ('book',)
#    list_per_page = 10

    fieldsets = (
            (None, {
                'fields': ('name', 'book', 'tag', 'credit', )
            }),
            ('Advanced options', {
                'classes': ('collapse',),
                'fields': ('alias',)
            }),
        )

class BookAdmin(admin.ModelAdmin):
    filter_horizontal = ('tag',)
    fields = ('title', 'language', 'credit', 'tag', 'annotation','book_file')

    list_per_page = 10
    search_fields = ('title', 'id')
#    filter_horizontal = ('tag', 'annotation', 'book_file')
    list_display = ('title', 'language', 'credit', 'id' )
    list_filter = ('credit',)
    raw_id_fields = ('annotation','book_file')


class AnnotationAdmin(admin.ModelAdmin):
    search_fields = ('name', )
    list_per_page = 10

class TagAdmin(admin.ModelAdmin):
    search_fields = ('name', )
    list_per_page = 10

class BookFileAdmin(admin.ModelAdmin):
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


