'''
Interface for adding authors, books for analizer.
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
    '''Handle request
      get inf from POST
      sent to action handler
      build response'''
#    if request.method != 'POST':
#        dict = {'status': 'error',
#                'code': 50001, 
#                'message': ERROR_CODE[50001]}
#    else:
#  
#        try:
#            xml_request = request.POST['xml']
#        except KeyError:
#            dict = {'status': 'error',
#                    'code': 10000,
#                    'message': ERROR_CODE[10000],}
#        else:        
#            try:
#                xml = etree.fromstring(xml_request)
#            except ExpatError, ex:
#                dict = {'status': 'error',
#                        'code': 10000,
#                        'message': 'XML parse error: ' + ex.__str__() }
#            else:
#                print 'xml is ok'
#                book_entr = load_book_entr_from_xml(xml)
#                dict = all_handler(action, book_entr)
#

#    book_entr_to_response(book_entrs)



        # strip string on strings, used delimiter='\r\n'
  #      data_dict = {}
  #      for item in request.POST.items():
  #          strs = convert_delim(split(item[1], '\r\n'))
  #          if len(strs) == 1:
  #              data_dict[item[0]] = strs[0]
  #          else:
  #              data_dict[item[0]] = strs
  #      
# #       for var in vars:
# #           try:
# #               x = data_dict[var]
# #            except KeyError:
# #               data_dict[var] = None
  #  
  #      # call handler
  #      try:
  #          dict = target(action, data_dict)
  #      except DataExcpt, excp:
  #          dict = excp.get_dict()
    
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
