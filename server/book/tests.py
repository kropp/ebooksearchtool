"""
This file demonstrates two different styles of tests (one doctest and one
unittest). These will both pass when you run "manage.py test".

Replace these with more appropriate tests for your application.
"""

import xml.etree.ElementTree as etree

from django.test import TestCase

from book.action_handler import *

class SimpleTest(TestCase):
    def test_basic_addition(self):
        """
        Tests that 1 + 1 always equals 2.
        """
        self.failUnlessEqual(1 + 1, 2)

class InsertBookTest(TestCase):
    def test_book_title(self):
        "Test title insert book"
        xml = etree.parse('book/test/test0.xml').getroot()
        print 'tipa'
        try:
            xml_exec_insert(xml)
        except InputDataServerException, e:
            print e
            self.failUnlessEqual(e.message, "Not found root tag 'book'")

__test__ = {"doctest": """
Another way to test that 1 + 1 is equal to 2.

>>> 1 + 1 == 2
True
"""}

