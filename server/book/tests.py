try:
    import xml.etree.ElementTree as etree
except ImportError:
    import elementtree.ElementTree as etree

from django.test import TestCase
from django.db.models import Q

from book.models import *
from book.action_handler import *
from book.get_action import get_by_id, get_q, make_q_from_tag, get_authors_q, get_files_q

from book.insert_action import get_authors, get_files

class GetActionTest(TestCase):
    def GetActionTest(self):
       pass 


    def test_get_q(self):
        q = get_q('title', 'query', 'icontains')
        qm = Q(title__icontains='query')
        self.failUnlessEqual(q.__str__(), qm.__str__())

        q = get_q('file__size', 'query')
        qm = Q(file__size='query')
        self.failUnlessEqual(q.__str__(), qm.__str__())


    def test_make_q_from_tag(self):
        # simple test
        xml_string = '<tag> text</tag>'
        xml = etree.fromstring(xml_string)
        q = make_q_from_tag(xml, 'tag')

        qm = Q(tag='text')
        self.failUnlessEqual(q.__str__(), qm.__str__())

        # test with search type
        xml_string = '<tag type="gt"> text </tag>'
        xml = etree.fromstring(xml_string)
        q = make_q_from_tag(xml, 'tag__name')

        qm = Q(tag__name__gt='text')
        self.failUnlessEqual(q.__str__(), qm.__str__())

        # test with search type and several object
        xml_string = '<tag id="4"> text </tag>'
        xml = etree.fromstring(xml_string)
        q = make_q_from_tag(xml, 'tag')

        qm = Q(tag__id='4')
        self.failUnlessEqual(q.__str__(), qm.__str__())


    def test_get_authors_q(self):
        xml_string = '''
        <authors>
            <author>name</author>
            <author type=""> name2 </author>
            <author id="4">name3</author>
        </authors>'''

        xml = etree.fromstring(xml_string)
        q = get_authors_q(xml)

        qm = Q(author__name__icontains='name') \
           & Q(author__name='name2') \
           & Q(author__id='4')
        self.failUnlessEqual(q.__str__(), qm.__str__())

    def test_get_files_q(self):
        xml_string = '''
        <files>
            <file><link>  http://link.ruosd</link></file>
            <file>
                <link>http://link.rutosd</link>
                <size>986</size>
            </file>
            <file type="less"> <size type="less">45</size> </file>
            <file id="4">name3</file>
        </files>'''


        xml = etree.fromstring(xml_string)
        q = get_files_q(xml)

        qm = Q(book_file__link='http://link.ruosd') \
           & Q(book_file__link='http://link.rutosd') \
           & Q(book_file__size='986') \
           & Q(book_file__size__less='45') \
           & Q(book_file__id='4')
        self.failUnlessEqual(q.__str__(), qm.__str__())
        


xml_string_insert_new = '''
<authors>
    <author>
        <name>Name</name>
        <alias> </alias>
        <alias> </alias>
    </author>
    <author>
        <name>  
        </name>
    </author>
    <author>
        <name>Name2 </name>
        <alias>Alias </alias>
    </author>
    <author>
        <name></name>
        <alias>Alias2</alias>
    </author>
    <author>
        <name>Name1</name>
        <alias>Alias6</alias>
        <alias>Alias7</alias>
        <alias>Alias8</alias>
    </author>
</authors>'''

class InsertActionTest(TestCase):

    def test_get_authors_inser_new(self):
        xml = etree.fromstring(xml_string_insert_new)
        authors = get_authors(xml)
#        authors.order_by(id)

        author = authors[0]
        self.failUnlessEqual(author.name, 'Name')
        self.failUnlessEqual(author.alias.count(), 0)

        author = authors[1]
        self.failUnlessEqual(author.name, 'Name2')
        self.failUnlessEqual(author.alias.all()[0].name, 'Alias')
        self.failUnlessEqual(author.alias.count(), 1)

        author = authors[2]
        self.failUnlessEqual(author.name, 'Name1')
        self.failUnlessEqual(author.alias.all()[0].name, 'Alias6')
        self.failUnlessEqual(author.alias.all()[1].name, 'Alias7')
        self.failUnlessEqual(author.alias.all()[2].name, 'Alias8')
        self.failUnlessEqual(author.alias.count(), 3)


    def test_get_authors_update_exists(self):
        xml = etree.fromstring(xml_string_insert_new)
        authors = get_authors(xml)
        xml_string = '''
        <authors>
            <author>
                <name>Name</name>
                <alias>Alias</alias>
            </author>
            <author>
                <name>  
                </name>
            </author>
            <author>
                <name>Name2</name>
                <alias>Alias </alias>
            </author>
            <author>
                <name></name>
                <alias>Alias2</alias>
            </author>
            <author>
                <name>Name1 </name>
                <alias>Alias6</alias>
                <alias>Alias7</alias>
                <alias>Alias9</alias>
            </author>
        </authors>'''
        
        xml = etree.fromstring(xml_string)
        authors = get_authors(xml)

        author = Author.objects.get(name='Name')
        alias = Alias.objects.get(name='Alias')
        self.failUnlessEqual(author.alias.all()[0].id, alias.id)

        author = Author.objects.get(name='Name1')
        self.failUnlessEqual(author.alias.count(), 4)
        self.failUnlessEqual(author.alias.all()[3].name, 'Alias9')


    def test_get_files_insert_new(self):
        "Tests insert new fiels"
        xml_string = '''
       <files>
        <file>
            <link> http://link  </link>
            <size></size>
            <type></type>
            <time_found></time_found>
            <last_check></last_check>

            <more_info></more_info>
            <img_link></img_link>
        </file>
       </files>
''';
        xml = etree.fromstring(xml_string)
        files = get_files(xml)

        file = BookFile.objects.get(link='http://link')
        self.failUnlessEqual(BookFile.objects.all()[0].id, file.id)
        self.failUnlessEqual(0, file.size)

    def test_get_files_check_size(self):
        "Tests checking size field"
        xml_string = '''
       <files>
        <file>
            <link> http://link  </link>
            <size>wrong</size>
        </file>
        <file>
            <link> http://link1  </link>
            <size>645</size>
        </file>
       </files>
''';
        xml = etree.fromstring(xml_string)
        files = get_files(xml)
        self.failUnlessEqual(BookFile.objects.all()[0].size, 0)
        self.failUnlessEqual(BookFile.objects.all()[1].size, 645)

    def test_get_files_check_empty(self):
        "Tests empty file if link is empty"
        xml_string = '''
       <files>
        <file>
            <link> </link>
            <size>84</size>
        </file>
       </files>
''';
        xml = etree.fromstring(xml_string)
        files = get_files(xml)
        self.failUnlessEqual(BookFile.objects.all().count(), 0)


