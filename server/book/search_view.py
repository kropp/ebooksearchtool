"View for search interface for analyzer"
# TODO marge this file with book.view
try:
    import xml.etree.ElementTree as etree
except ImportError:
    import elementtree.ElementTree as etree
from xml.parsers.expat import ExpatError
import logging
from sys import exc_info

from django.shortcuts import render_to_response
from django.template import Context

try:
    from settings import ANALYZER_DEBUG_MODE
except ImportError:
    ANALYZER_DEBUG_MODE = False

from spec.exception import RequestFileServerException, \
ServerException, InnerServerException
import spec.logger

from book.search import xml_search

from book.request_check import check_ip, get_xml_request

MAIN_LOG = logging.getLogger("main_logger")

def search_view(request):
    "View for search interface for analyzer"
    try:

        messages = []

        check_ip(request)
        xml_request = get_xml_request(request)

        # parse request
        try:
            xml = etree.fromstring(xml_request.encode('utf-8'))
        except ExpatError, ex:
            raise RequestFileServerException(ex.message)


        # execute search request
        (entity_type, entities) = xml_search(xml)
        for entity in entities:
            entity.sphinx_weight = entity._sphinx['weight']
        return render_to_response('data/search.xml', \
                                  Context({entity_type: entities,}))

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
    
