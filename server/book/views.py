'''
'''

import xml.etree.ElementTree as etree
from string import split
from xml.parsers.expat import ExpatError

from django.core.exceptions import *
from django.http import HttpResponse
from django.shortcuts import render_to_response
from django.template import Context

from server.spec.utils import convert_delim
from server.exception import *
from server.book.action_handler import xml_exec_get, xml_exec_insert, ACTION

def books_to_response(books):
    '''function convert book_entirety to response'''

    return render_to_response('data/analyser_response.xml', {'books': books})




def data_modify(request, action):
    '''Gets inf from POST, sends to action handler, builds response'''

    dict = {}

    try:
        if request.method != 'POST':
            raise RequestServerException('Use POST method in request')

        try:
            xml_request = request.POST['xml']
        except KeyError:
            raise RequestServerException("Not found 'xml' field in POST request")

        try:
            xml = etree.fromstring(xml_request)
        except ExpatError, ex:
            raise RequestFileServerException(ex.message)

        if action == ACTION['get']:
            books = xml_exec_get(xml)
            return books_to_response(books)
        elif action == ACTION['insert']:
            result = xml_exec_insert(xml)
            pass
        else:
            # TODO insert error here
            pass

        dict['message'] = 'ok'
            

    except ServerException, ex:
        dict['error'] = ex.__doc__
        dict['class'] = ex.__class__
        dict['message'] = ex.message
#    except Exception, ex:
#     #   dict['error'] = 'Unknown error: ' + ex.__doc__
#        dict['class'] = ex.__class__
#        dict['message'] = ex.message
        
    return render_to_response('data/main_response.xml', Context(dict))
