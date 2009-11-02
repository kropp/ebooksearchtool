"""
This file demonstrates two different styles of tests (one doctest and one
unittest). These will both pass when you run "manage.py test".

Replace these with more appropriate tests for your application.
"""

import xml.etree.ElementTree as etree

from django.test import TestCase

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
        xml_string = '<authors> <author>author</author> <author id="2222" /> </authors>'
        xml = etree.fromstring(xml_string)
        al = Author(id=2222, name='s')
        al.save()
        a2 = Author(name='The Author')
        a2.save()
        a3 = Author(name='author tia')
        a3.save()
        a4 = Author(name='author2 tia')
        a4.save()

        a_list = [a2, a3, a4, al]
        q = get_author_queryset(xml)
        a_list2 = Author.objects.filter(q).order_by('id')

        self.failUnlessEqual(a_list, a_list2)




