''' middleware add bottom to our templates '''

from django.shortcuts import render_to_response
from settings import EBST_NAME, EBST_VERSION, EBST_VERSION_BUILD

class BoottomMiddleware(object):
    def process_response(self, request, response):
        bottom_string = "%s v%s (build %s)" % (EBST_NAME, EBST_VERSION, EBST_VERSION_BUILD)
        google_link = 'http://code.google.com/p/ebooksearchtool/'
        bottom = render_to_response('book/xhtml/bottom.xml',
            {'bottom_string': bottom_string, 'google_link':google_link})

        response.write(bottom.content)
        return response
