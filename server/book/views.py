'''
'''

import xml.etree.ElementTree as etree
from string import split
from xml.parsers.expat import ExpatError

from django.http import HttpResponse
from django.shortcuts import render_to_response
from django.template import Context

from server.spec.utils import convert_delim
from server.exception import *
from server.book.action_handler import all_handler, load_book_entr_from_xml

def book_entr_to_response(book_entrs):
    '''function convert book_entirety to response'''

    return render_to_response('data/analyser_response.xml', {'book_entrs': book_entrs})




def data_modify(request, action):
    '''Gets inf from POST, sends to action handler, builds response'''

    dict = {}

    try:
        if request.method != 'POST':
            raise RequestServerEx('Use POST method in request')

        try:
            xml_request = request.POST['xml']
        except KeyError:
            raise RequestServerEx("Not found 'xml' field in POST request")

        try:
            xml = etree.fromstring(xml_request)
        except ExpatError, ex:
            raise RequestFileServerEx(ex.message)

        print 'xml is ok'
        book_entr = load_book_entr_from_xml(xml)

        print 'load from xml ... ok'
        book_entrs = all_handler(action, book_entr)
        print 'action handler ok'
        dict['message'] = 'ok'
        for book in book_entrs:
            print "AAA", book
        return book_entr_to_response(book_entrs)

    except ServerEx, ex:
        dict['error'] = ex.__doc__
        dict['class'] = ex.__class__
        dict['message'] = ex.message
    except Exception, ex:
     #   dict['error'] = 'Unknown error: ' + ex.__doc__
        dict['class'] = ex.__class__
        dict['message'] = ex.message
        
    return render_to_response('data/main_response.xml', Context(dict))
