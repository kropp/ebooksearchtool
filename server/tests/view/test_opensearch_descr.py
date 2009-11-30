from django.test import TestCase
from django.test.client import Client

class OpenTestCase(TestCase):
    def test_opensearch(self):
        client = Client()
        response = client.get('/opensearch/sssss')
        print response.status_code
        self.failUnlessEqual(response.status_code, 200)


