
try:
    import xml.etree.ElementTree as etree
except ImportError:
    import elementtree.ElementTree as etree

try:
    from hashlib import md5
except ImportError:
    import md5

from django.test import TestCase
from django.db.models import Q
from django.db import IntegrityError


from book.models import *


from spec.exception import InputDataServerException

from book.update_entity import *

XML_STRING = """
<a id="2">
    <b ui="4">
        b_tag_text
        <c ui="1">
            c_tag_text
        </c>
    </b>
    <d> d_tag_text </d>
    <e id="45"> </e>
    <t id="t5"> </t>
</a>
"""

class XmlUtilsTest(TestCase):
    
    def setUp(self):
        self.xml = etree.fromstring(XML_STRING)


    def test_get_tag_text(self):
        text = get_tag_text(self.xml, 'a')
        self.failUnlessEqual(text, None)

        text = get_tag_text(self.xml, 'e')
        self.failUnlessEqual(text, None)

        text = get_tag_text(self.xml, 'd')
        self.failUnlessEqual(text, 'd_tag_text')


    def test_get_id(self):
        self.failUnlessEqual(get_id(self.xml), 2)
        self.failUnlessEqual(get_id(self.xml.find('e')), 45)
        self.assertRaises(InputDataServerException, get_id, self.xml.find('t'))
        


class UpdateEntityTest(TestCase):

    def setUp(self):
        self.xml_create_author_epmty = etree.fromstring("""
        <author ui="3">
            <full_name> </full_name>
        </author>
        """)
        self.xml_create_author = etree.fromstring("""
        <author ui="3">
            <full_name> author </full_name>
        </author>
        """)

    def test_update_author(self):
        # Test creating new author
        author = update_author(self.xml_create_author)
        db_author = Author.objects.get(id=author.id)
        self.failUnlessEqual(db_author.name, 'author')
        self.failUnlessEqual(author.__dict__, db_author.__dict__)

        # Test raising exception on creating author with existing name
        self.assertRaises(IntegrityError, update_author, self.xml_create_author)
    
        # Test raising exception on creating author with empty field 'name'
        self.assertRaises(IntegrityError, update_author, \
                          self.xml_create_author_epmty)

