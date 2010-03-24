from book.models import *

class AuthorAdmin(admin.ModelAdmin):
    search_fields = ('name', )
    filter_horizontal = ('tag', 'book')
    list_display = ('name', 'credit', 'id' )

class BookAdmin(admin.ModelAdmin):
    search_fields = ('title', 'id')
    filter_horizontal = ('tag', 'annotation', 'book_file')
    list_display = ('title', 'language', 'credit', 'id' )

class AnnotationAdmin(admin.ModelAdmin):
    search_fields = ('name', )

class TagAdmin(admin.ModelAdmin):
    search_fields = ('name', )

class BookFileAdmin(admin.ModelAdmin):
    search_fields = ('type', 'link')
    list_display = ('link', 'type', 'credit', 'id')
    list_filter = ('last_check',)

admin.site.register(Annotation, AnnotationAdmin)

admin.site.register(Book, BookAdmin)

admin.site.register(Author, AuthorAdmin)

admin.site.register(BookFile, BookFileAdmin)

admin.site.register(Tag, TagAdmin)

#admin.site.register(Series)

#admin.site.register(AuthorAlias)


