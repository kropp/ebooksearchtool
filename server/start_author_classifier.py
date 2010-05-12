'''script for starting author classifier '''

from django.core.management import setup_environ
import settings
setup_environ(settings)

import classifier.search_tools as search_tools
from book.models import Author

def tag_author_adding(authors):
    counter = 0
    counter_all = 0
    for author in authors:
        counter_all += 1
        flag = search_tools.search_for_author_information(author)
        if flag:
            counter += 1

        if not counter%10:
            print counter, "of cheched ", counter_all

    print "Classification complete"
    print counter, " authors has been classified"

AUTHORS = Author.objects.exclude(tag__isnull=False)
tag_author_adding(AUTHORS)
