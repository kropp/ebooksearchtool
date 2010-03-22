from book.models import *

class AuthorAdmin(admin.ModelAdmin):
    search_fields = ('name', )
    filter_horizontal = ('tag',)

admin.site.register(Annotation)

admin.site.register(Book)

admin.site.register(Author, AuthorAdmin)

#admin.site.register(BookFile)

#admin.site.register(Series)

#admin.site.register(AuthorAlias)

admin.site.register(Tag)
