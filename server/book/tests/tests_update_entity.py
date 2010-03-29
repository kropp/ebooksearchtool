
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
        Language(short='?').save()
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
        self.xml_create_book_file_epmty = etree.fromstring("""
        <book_file ui="2" />
        """)
        self.xml_create_book_file  = etree.fromstring("""
        <book_file ui="2">
            <link>link </link>
            <size> 4 </size>
            <credit> 5 </credit>
        </book_file>
        """)
        self.xml_update_book_file  = etree.fromstring("""
        <book_file>
            <link>link1 </link>
            <size> 3 </size>
            <credit> 1 </credit>
        </book_file>
        """)

        self.xml_create_book = etree.fromstring("""
        <book>
            <title> title</title>
            <authors>
                <author id="1" />
                <author id="2" />
            </authors>
        </book>
        """)
        self.xml_create_book_empty_title = etree.fromstring("""
        <book>
            <title></title>
            <authors>
                <author id="1" />
                <author id="2" />
            </authors>
        </book>
        """)
        self.xml_create_book_empty_author = etree.fromstring("""
        <book>
            <title> title</title>
            <authors>
            </authors>
        </book>
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

    def test_update_book_file(self):
        self.assertRaises(IntegrityError, update_book_file, \
                          self.xml_create_book_file_epmty)

        # test create new book_file
        book_file = update_book_file(self.xml_create_book_file)
        db_book_file = BookFile.objects.get(id=book_file.id)
        self.failUnlessEqual(db_book_file.link, 'link')
        self.failUnlessEqual(db_book_file.size, 4)
        self.failUnlessEqual(db_book_file.credit, 5)

        # Test rising exception on creating book_file with existing link
        self.assertRaises(IntegrityError, update_book_file, \
                          self.xml_create_book_file)

        # test update existing book_file
        self.xml_update_book_file.attrib['id'] = db_book_file.id

        book_file = update_book_file(self.xml_update_book_file)
        self.failUnlessEqual(book_file.link, 'link1')
        self.failUnlessEqual(book_file.size, 3)
        self.failUnlessEqual(book_file.credit, 1)
        self.failUnlessEqual(book_file.id, db_book_file.id)

    
    def test_create_book(self):
        self.assertRaises(IntegrityError, update_book, \
                          self.xml_create_book_empty_title)
        a1 = Author.objects.get_or_create(id=1, name='author1')[0]
        a2 = Author.objects.get_or_create(id=2, name='author2')[0]
        book = update_book(self.xml_create_book)
        self.failUnlessEqual(book.title, 'title')
        self.failUnlessEqual(set(book.author_set.all()), set([a1,a2]))


