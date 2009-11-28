"Views for analyzer"
try:
    import xml.etree.ElementTree as etree
except ImportError:
    import elementtree.ElementTree as etree
from xml.parsers.expat import ExpatError
import logging

from django.shortcuts import render_to_response
from django.template import Context

from spec.exception import RequestFileServerException, \
RequestServerException, ServerException
from book.action_handler import ACTION
from book.insert_action import xml_exec_insert
from book.get_action import xml_exec_get

MAIN_LOG = logging.getLogger("main_logger")

def data_modify(request, action):
    '''Gets inf from POST, sends to action handler, builds response'''
    print "+++ data modify +++"
    context_dict = {}
    messages = []

    MAIN_LOG.info("Got connection")

    try:
        # check request
        if request.method != 'POST':
            raise RequestServerException('Use POST method in request')
        try:
            xml_request = request.POST['xml']
        except KeyError:
            raise RequestServerException("Not found xml field in POST request")

        # parse request
        try:
            xml = etree.fromstring(xml_request)
        except ExpatError, ex:
            raise RequestFileServerException(ex.message)

        # execute request
        if action == ACTION['get']:
            books = xml_exec_get(xml)
            return render_to_response('data/analyser_response.xml', \
                                      {'books': books})
        elif action == ACTION['insert']:
            messages = xml_exec_insert(xml)
            context_dict['message'] = 'ok'
        else:
            # TODO insert error here
            pass


    except ServerException, ex:
        context_dict['error'] = ex.__doc__
        context_dict['class'] = ex.__class__
        context_dict['message'] = ex.message
        MAIN_LOG.warning(ex)
    except Exception, ex:
     #   dict['error'] = 'Unknown error: ' + ex.__doc__
        context_dict['class'] = ex.__class__
        context_dict['message'] = ex.message
        MAIN_LOG.warning(ex)
        
    return render_to_response('data/main_response.xml', Context(context_dict))
