'''script for starting book classifier '''

from django.core.management import setup_environ
import settings
setup_environ(settings)

import classifier.tools as tools

tools.add_tag_for_new_books()

