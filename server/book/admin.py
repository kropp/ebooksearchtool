from book.models import *

class AuthorAdmin(admin.ModelAdmin):
    search_fields = ('name', )
#    filter_horizontal = ('tag',)
    filter_horizontal = ('tag', 'book')
    list_display = ('name', 'credit', 'id' )
    list_filter = ('credit',)
#    raw_id_fields = ('book',)

class BookAdmin(admin.ModelAdmin):
    search_fields = ('title', 'id')
    filter_horizontal = ('tag', 'annotation', 'book_file')
#    filter_horizontal = ('tag',)
    list_display = ('title', 'language', 'credit', 'id' )
    list_filter = ('credit',)
    fields = ('title', 'language', 'credit', 'annotation', 'book_file', 'series', 'tag')
#    raw_id_fields = ('annotation','book_file')


class AnnotationAdmin(admin.ModelAdmin):
    search_fields = ('name', )

class TagAdmin(admin.ModelAdmin):
    search_fields = ('name', )

class BookFileAdmin(admin.ModelAdmin):
    search_fields = ('type', 'link')
    list_display = ('link', 'type', 'credit', 'id')
    list_filter = ('last_check', 'credit')


admin.site.register(Annotation, AnnotationAdmin)

admin.site.register(Book, BookAdmin)

admin.site.register(Author, AuthorAdmin)

admin.site.register(BookFile, BookFileAdmin)

admin.site.register(Tag, TagAdmin)

#admin.site.register(Series)

#admin.site.register(AuthorAlias)


