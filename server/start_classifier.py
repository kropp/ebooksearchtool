'''script for starting book classifier '''

from django.core.management import setup_environ
import settings
setup_environ(settings)

import classifier.tools as tools

tools.tag_adding()

