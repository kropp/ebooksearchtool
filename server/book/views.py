try:
    import xml.etree.ElementTree as etree
except ImportError:
    import elementtree.ElementTree as etree
from xml.parsers.expat import ExpatError
from string import split

from django.core.exceptions import *
from django.http import HttpResponse
from django.shortcuts import render_to_response
from django.template import Context

from spec.utils import convert_delim
from spec.exception import *
from book.action_handler import ACTION
from book.insert_action import xml_exec_insert
from book.get_action import xml_exec_get

def books_to_response(books):
    '''function convert book_entirety to response'''

    return render_to_response('data/analyser_response.xml', {'books': books})




def data_modify(request, action):
    '''Gets inf from POST, sends to action handler, builds response'''
    print "+++ data modify +++"
    dict = {}
    messages = []

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
            messages = xml_exec_insert(xml)
        else:
            # TODO insert error here
            pass

        dict['message'] = 'ok'
            

    except ServerException, ex:
        print ex
        dict['error'] = ex.__doc__
        dict['class'] = ex.__class__
        dict['message'] = ex.message
    except Exception, ex:
        print ex
     #   dict['error'] = 'Unknown error: ' + ex.__doc__
        dict['class'] = ex.__class__
        dict['message'] = ex.message
        
    return render_to_response('data/main_response.xml', Context(dict))
