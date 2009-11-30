import unittest
from django.test.client import Client

class OpenTestCase(unittest.TestCase):
    def test_opensearch(self):
        client = Client()
        response = client.get('/opensearch')
        self.failUnlessEqual(response.status_code, 200)


