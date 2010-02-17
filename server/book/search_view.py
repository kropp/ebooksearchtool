
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

from settings import ANALYZER_IP, EBST_NAME, EBST_VERSION, EBST_VERSION_BUILD

try:
    from settings import ANALYZER_DEBUG_MODE
except ImportError:
    ANALYZER_DEBUG_MODE = False

from spec.exception import RequestFileServerException, \
RequestServerException, ServerException, InnerServerException
from book.insert_action import xml_exec_insert
from book.get_action import xml_exec_get
import spec.logger

from book.search import xml_search

MAIN_LOG = logging.getLogger("main_logger")

def search_view(request):
    try:

        messages = []

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


        # TODO insert code here
        authors = xml_search(xml)
        for author in authors:
            author.sphinx_weight = author._sphinx['weight']
        return render_to_response('data/author_search.xml', Context({'authors': authors,}))

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
        if not ANALYZER_DEBUG_MODE:
            exception_type, exception_value, exception_traceback = exc_info()
            msg_body = "%s: %s" % (exception_type, exception_value)
            messages.append(('error', msg_body))
            MAIN_LOG.exception(ex)
        else:
            raise
        
        
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
        'messages': messages,
    }

    return render_to_response('data/main_response.xml', Context(context_dict))
    
