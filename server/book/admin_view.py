from django.shortcuts import render_to_response
from book.models import Book, Author, Tag, Language

def author(request,author_id):
    tags = Tag.objects.all().order_by("name")
    if id == '':
        return render_to_response(
                "admin/author.html", {'id': 0,}
            )
    else:
        author = Author.objects.get(id = author_id)
        return render_to_response(
            "admin/author.html", {'author': author, 'tags': tags}
        )

