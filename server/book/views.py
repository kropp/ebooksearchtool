"Views for analyzer"
# -*- coding: utf-8 -*-

try:
    import xml.etree.ElementTree as etree
except ImportError:
    import elementtree.ElementTree as etree
from xml.parsers.expat import ExpatError
import logging
from sys import exc_info
from traceback import print_exc

from django.shortcuts import render_to_response
from django.template import Context

from settings import ANALYZER_IP
from spec.exception import RequestFileServerException, \
RequestServerException, ServerException, InnerServerException
from book.insert_action import xml_exec_insert
from book.get_action import xml_exec_get
import spec.logger

MAIN_LOG = logging.getLogger("main_logger")


ACTION = {
    'get': 1,
    'insert': 2,
}

def who():
    '''Generates default page'''
    pass


def data_modify(request, action):
    '''Gets inf from POST, sends to action handler, builds response'''
    context_dict = {}
    messages = []

    MAIN_LOG.info("Got connection from analyzer")
    messages.append(('debug', 'Starting'))

    try:

        # check IP adress
        if ANALYZER_IP and request.META['REMOTE_ADDR'] != ANALYZER_IP:
            raise RequestServerException('Bad IP adress. Your IP is %s' %
                                            (request.META['REMOTE_ADDR']))

        # check request
        if request.method != 'POST':
            raise RequestServerException('Use POST method in request')
        try:
            xml_request = request.POST['xml']
        except KeyError:
            raise RequestServerException("Not found xml field in POST request")

        # parse request
        try:
            xml = etree.fromstring(xml_request.encode('utf-8'))

        except ExpatError, ex:
            raise RequestFileServerException(ex.message)

        # execute request
        if action == ACTION['get']:
            books = xml_exec_get(xml)
            return render_to_response('data/analyser_response.xml', \
                                      {'books': books})
        elif action == ACTION['insert']:
            messages = xml_exec_insert(xml)
        else:
            raise InnerServerException("Unknow action")

    except InnerServerException, ex:
        exception_type, exception_value, exception_traceback = exc_info()
        msg_body = "%s: %s" % (exception_type, exception_value)
        messages.append(('error', msg_body))
        MAIN_LOG.exception(ex)

    except ServerException:
        exception_type, exception_value, exception_traceback = exc_info()
        msg_body = "%s: %s" % (exception_type, exception_value)
        messages.append(('error', msg_body))
        MAIN_LOG.warning(msg_body)

    except Exception, ex:
        exception_type, exception_value, exception_traceback = exc_info()
        msg_body = "%s: %s" % (exception_type, exception_value)
        messages.append(('error', msg_body))
        MAIN_LOG.exception(ex)
        
    # computation of operation status 
    msg_types = (msg_type for msg_type, msg in messages)
    if 'error' in msg_types:
        status = 'error'
    elif 'warning' in msg_types:
        status = 'warning'
    else:
        status = 'ok'

    # load all vars to context
    context_dict = {
        'status': status,
        'messages': messages,}

    return render_to_response('data/main_response.xml', Context(context_dict))
