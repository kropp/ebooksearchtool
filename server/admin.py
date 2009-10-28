from django.contrib import admin
from book.models import *

class AnnotationAdmin(admin.ModelAdmin):
    pass
admin.site.register(Annotation, AnnotationAdmin)

class BookAdmin(admin.ModelAdmin):
    pass
admin.site.register(Book, BookAdmin)

class AuthorAdmin(admin.ModelAdmin):
    pass
admin.site.register(Author, AuthorAdmin)

class BookFileAdmin(admin.ModelAdmin):
    pass
admin.site.register(BookFile, BookFileAdmin)

class SeriesAdmin(admin.ModelAdmin):
    pass
admin.site.register(Series, SeriesAdmin)

class AliasAdmin(admin.ModelAdmin):
    pass
admin.site.register(Alias, AliasAdmin)

class TagAdmin(admin.ModelAdmin):
    pass
admin.site.register(Tag, TagAdmin)
