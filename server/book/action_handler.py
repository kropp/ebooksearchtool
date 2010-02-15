'''Action handler'''

import md5

from django.db import IntegrityError
from django.db import transaction
from django.core.exceptions import *

from spec.utils import replace_delim_to_space
from spec.exception import *
from book.models import *
from spec.logger import main_logger


