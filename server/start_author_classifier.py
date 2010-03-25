from django.core.management import setup_environ
import settings
setup_environ(settings)

import classifier.search_tools as search_tools
from book.models import Author

authors = Author.objects.all()

for author in authors:
    if author.tag.count() == 0:
        search_tools.search_for_author_information(author)
