#! /usr/bin/env python

"Generates sphinx config file from templates and project settings."

import os
import sys

from django.core.management import setup_environ
import settings
setup_environ(settings)

from django.template.loader import get_template
from django.template import Context


SPHINX_CONF_TEMPLATE = 'sphinx_conf/sphinx.conf'

WARNING_MESSAGE = \
"""
WARNING: don't modify this file!
This file was automatically generated by '%s'
from templates/sphinx_conf templates files.


""" % os.path.abspath(sys.argv[0])

def generate_config():
    "Returns config generated from templates."
    context = {
        'SPHINX_HOST': getattr(settings, 'SPHINX_HOST', '127.0.0.1'),
        'SPHINX_PORT': getattr(settings, 'SPHINX_PORT', '3312'),

        'SPHINX_LOG_PATH': \
            os.path.abspath(getattr(settings, 'SPHINX_LOG_PATH', '/var/log')),
        'SPHINX_INDEX_PATH': \
            os.path.abspath(getattr(settings, 'SPHINX_INDEX_PATH', \
                                    '/var/data')),
    }
    if getattr(settings, 'DATABASES', None):
        context.update({
            'DATABASE_ENGINE': settings.DATABASES['default']['ENGINE'],
            'DATABASE_HOST': settings.DATABASES['default']['HOST'],
            'DATABASE_PASSWORD': settings.DATABASES['default']['PASSWORD'],
            'DATABASE_USER': settings.DATABASES['default']['USER'],
            'DATABASE_PORT': settings.DATABASES['default']['PORT'],
            'DATABASE_NAME': settings.DATABASES['default']['NAME'],
        })
    else:
        context.update({
            'DATABASE_ENGINE': settings.DATABASE_ENGINE,
            'DATABASE_HOST': settings.DATABASE_HOST,
            'DATABASE_PASSWORD': settings.DATABASE_PASSWORD,
            'DATABASE_USER': settings.DATABASE_USER,
            'DATABASE_PORT': settings.DATABASE_PORT,
            'DATABASE_NAME': settings.DATABASE_NAME,
        })

    template = get_template(SPHINX_CONF_TEMPLATE)
    return template.render(Context(context))


if __name__ == "__main__":
    CONFIG_STRING = WARNING_MESSAGE + generate_config()
    # writing database conf to sphinx conf file
    SPHINX_CONF_DIR = getattr(settings, 'SPHINX_CONF_DIR', '.')
    SPHINX_CONF_FILE = getattr(settings, 'SPHINX_CONF_FILE', 'sphinx.conf')

    OUT_FILE = open(os.path.join(SPHINX_CONF_DIR, SPHINX_CONF_FILE), 'w')
    OUT_FILE.write(CONFIG_STRING)
    OUT_FILE.close()
