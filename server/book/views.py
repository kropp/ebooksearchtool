'''
Interface for adding authors, books for analizer.
'''

from string import split
import xml.etree.ElementTree as etree

from django.http import HttpResponse
from django.shortcuts import render_to_response
from django.template import Context

from server.spec.errorcode import ERROR_CODE
from server.spec.utils import convert_delim
from server.exception import *


class Test:
    class T:
        def __init__(self, str):
            print str



def data_modify(request, action, target):
    '''Handle request
      get inf from POST
      sent to action handler
      build response'''
    if request.method != 'POST':
        dict = {'status': 'error',
                'code': 50001, 
                'message': ERROR_CODE[50001]}
    else:
  
#        try:
#            xml_request = request.POST['xml']
#        except KeyError:
#            dict = {'status': 'error',
#                    'code': 10000,
#                    'message': ERROR_CODE[10000],}
#        else:        
#            xml = etree.fromstring(xml_request)
        

        # strip string on strings, used delimiter='\r\n'
        data_dict = {}
        for item in request.POST.items():
            strs = convert_delim(split(item[1], '\r\n'))
            if len(strs) == 1:
                data_dict[item[0]] = strs[0]
            else:
                data_dict[item[0]] = strs
    
        # call handler
        try:
            dict = target(action, data_dict)
        except DataExcpt, excp:
            dict = excp.get_dict()
  
    return render_to_response('data/main_response.xml', Context(dict))
