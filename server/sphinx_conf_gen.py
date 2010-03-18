#! /usr/bin/env python

"Generates sphinx config file from templates and project settings."

import os
import glob

from settings import DATABASE_ENGINE, DATABASE_HOST, DATABASE_PORT
from settings import DATABASE_NAME, DATABASE_USER, DATABASE_PASSWORD

SPHINX_CONF_DIR = "spec/sphinx_conf"
SPHINX_CONF_FILE = "sphinx.conf"

TEMPLATE = """
# database settings
# DON'T MODIFY this file
# its file generated automatically by sphinx_conf_gen.py

source database_settings
{
    type                = %s
    sql_host            = %s
    sql_port            = %s
    sql_db              = %s
    sql_user            = %s
    sql_pass            = %s

    sql_query_pre       =
    sql_query_post      =
}
"""

FILE_HEADER = """
#
# Content from file %s
#
"""

def get_database_conf():
    "Returns string with database config for sphinx config."
    result_str = TEMPLATE % (DATABASE_ENGINE, DATABASE_HOST, DATABASE_PORT, \
                             DATABASE_NAME, DATABASE_USER, DATABASE_PASSWORD)
    return result_str


if __name__ == '__main__':
    # generate database conf
    database_conf = get_database_conf()
    # writing database conf to sphinx conf file
    out_file = open(os.path.join(SPHINX_CONF_DIR, SPHINX_CONF_FILE), 'w')
    out_file.write(database_conf)

    # merge all templates
    # get list of merging files
    template_files = glob.glob(os.path.join(SPHINX_CONF_DIR, '*.tmplt'))
    template_files.sort()

    for in_file_name in template_files:
        # write header before file content
        out_file.write( FILE_HEADER % (in_file_name,) )
        in_file = open(in_file_name)
        # read and write file content
        out_file.write( in_file.read() )
        in_file.close()

    out_file.close()
