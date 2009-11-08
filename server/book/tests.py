

import xml.etree.ElementTree as etree

from django.test import TestCase
from django.db.models import Q

from book.models import *
from book.action_handler import *
from book.get_action import get_by_id, get_author_queryset, get_file_queryset, get_book_queryset

def exception_catcher(f, exception):
    '''Trys to exec f() and except exeption;
    if exception isn't raised, then raises Exception
    return catched exception'''
    try:
        f()
    except exception, e:
        return e
    raise Exception('Uncatched exception %s' % (exception))



class GetActionTest(TestCase):
    def GetActionTest(self):
       pass 

    def test_get_by_id(self):
        print 1

        # tests return of correct object by id
        bs = Book(title='title')
        bs.save()
        xml_string = '<tag id="1"></tag>'
        xml = etree.fromstring(xml_string)
        b = get_by_id(Book, xml)
        self.failUnlessEqual(b, bs)
        
        # tests exception on unexisting id
        xml_string = '<tag id="99991"></tag>'
        xml = etree.fromstring(xml_string)
        e = exception_catcher(lambda: get_by_id(Book, xml), InputDataServerException)
        self.failUnlessEqual('The Book with id = 99991 does not exist in database', e.message)
        
        # tests return None by undefined id
        xml_string = '<tag></tag>'
        xml = etree.fromstring(xml_string)
        b = get_by_id(Book, xml)
        self.failUnlessEqual(b, None)
        
        # tests excepion on bad id type (not int)
        xml_string = '<tag id="badId"></tag>'
        xml = etree.fromstring(xml_string)
        e = exception_catcher(lambda: get_by_id(Book, xml), InputDataServerException)
        self.failUnlessEqual('The Book id must be int', e.message)


    def test_get_author_quesy_set(self):
        print 2
        a = Author(name='authowqwr')
        a.save()

        xml_string = '<authors> <author> author  </author> <author id="%s" /> <author> tipa</author></authors>' % (a.id)
        xml = etree.fromstring(xml_string)

        q = Q(author__name__icontains='author') \
          & Q(author__id=a.id) \
          & Q(author__name__icontains='tipa')

        qf = get_author_queryset(xml)

        self.failUnlessEqual(q.__str__(), qf.__str__())




