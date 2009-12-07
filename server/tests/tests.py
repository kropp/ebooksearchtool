"""
This file demonstrates two different styles of tests (one doctest and one
unittest). These will both pass when you run "manage.py test".

Replace these with more appropriate tests for your application.
"""

from django.test import TestCase
from django.test.client import Client

from book.models import Author
from book.models import Book

class OpenTestCase(TestCase):
    def test_opensearch(self):
        client = Client()
        response = client.get('/opensearch/')
        print 'status code for open search', response.status_code
        self.failUnlessEqual(response.status_code, 200)
        
    def test_all_books_xhtml(self):
        client = Client()
        response = client.get('/all/')
        print 'status code for all books', response.status_code
        self.failUnlessEqual(response.status_code, 200)   

    def test_all_books_opds(self):
        client = Client()
        response = client.get('/all.atom/')
        print 'status code for all books in opds', response.status_code
        self.failUnlessEqual(response.status_code, 200)         

    def test_catalog_xhtml(self):
        client = Client()
        response = client.get('/catalog/')
        print 'status code for catalog', response.status_code
        self.failUnlessEqual(response.status_code, 200)    
        
    def test_catalog_opds(self):
        client = Client()
        response = client.get('/catalog.atom/')
        print 'status code for catalog in opds', response.status_code
        self.failUnlessEqual(response.status_code, 200)   
        
    def test_author(self):
        client = Client()
        author = Author(name="Author")
        author.save()
        author_id = author.id

        response = client.get('/author.atom/id%s/' %(author_id,))
        print 'status code for good author request in atom', response.status_code
        self.failUnlessEqual(response.status_code, 200)
        
        response = client.get('/author/id%s/' %(author_id,))
        print 'status code for good author request in xhtml', response.status_code
        self.failUnlessEqual(response.status_code, 200)

        response = client.get('/author.atom/id%s/' %(author_id + 1,))
        print 'status code for bad author request in atom', response.status_code
        self.failUnlessEqual(response.status_code, 404)                          

        response = client.get('/author/id%s/' %(author_id + 1,))
        print 'status code for bad author request in xhtml', response.status_code
        self.failUnlessEqual(response.status_code, 404)                          

    def test_book(self):
        client = Client()
        book = Book(title="Book")
        book.save()
        book_id = book.id
        
        response = client.get('/book.atom/id%s/' %(book_id,))
        print 'status code for good book request in atom', response.status_code
        self.failUnlessEqual(response.status_code, 200)

        response = client.get('/book/id%s/' %(book_id,))
        print 'status code for good book request in xhtml', response.status_code
        self.failUnlessEqual(response.status_code, 200)

        response = client.get('/book.atom/id%s/' %(book_id + 1,))
        print 'status code for bad book request in atom', response.status_code
        self.failUnlessEqual(response.status_code, 404)                          
        
        response = client.get('/book/id%s/' %(book_id + 1,))
        print 'status code for bad book request in xhtml', response.status_code
        self.failUnlessEqual(response.status_code, 404)                   
        
    def test_search(self):
        client = Client()
        book = Book(title="Book")
        book.save()
        
        response = client.get('/search?query=Book')        
        print 'status code for good search request', response.status_code
        self.failUnlessEqual(response.status_code, 200)
        
        response = client.get('/search.atom?query=Book')        
        print 'status code for good search request', response.status_code
        self.failUnlessEqual(response.status_code, 200)
        
    def test_empty_search(self):
        client = Client()        
        response = client.get('/search')        
        expected_response = open("./tests/expected_response/empty_search.xml", "rb").read()
        self.failUnlessEqual(response.content, expected_response)

