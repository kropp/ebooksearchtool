from book.models import *

class AuthorAdmin(admin.ModelAdmin):
    search_fields = ('name', )
    filter_horizontal = ('tag', 'book')

class BookAdmin(admin.ModelAdmin):
    search_fields = ('title', )
    filter_horizontal = ('tag', 'annotation', 'book_file')


admin.site.register(Annotation)

admin.site.register(Book, BookAdmin)

admin.site.register(Author, AuthorAdmin)

admin.site.register(BookFile)

#admin.site.register(Series)

#admin.site.register(AuthorAlias)

admin.site.register(Tag)
