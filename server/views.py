# -*- coding: utf-8 -*-
from django.contrib.syndication.feeds import Feed
from book.models import Book
from django.utils.feedgenerator import Atom1Feed
from django.utils.xmlutils import SimplerXMLGenerator

from django.http import HttpResponse
from book.models import Author
def my_test(request, add_author = ''):
 #   a = Author()
 #   a.name = u"Ремарк, Эрих Мария"
 #   a.save()
    if add_author:
      a = Author()
      a.name = add_author
      a.save()

    a_list = Author.objects.all()
    html = ''
    for a in a_list:
        html += a.name + "<br>"
    
    return HttpResponse(html)

class OPDSFeed(Atom1Feed ):
    "Feed class to support OPDS protocol"
    
    def __init__(self, title, link, description, total=None, item_language=None, items_per_page=None, language=None, author_email=None,
            author_name=None, author_link=None, subtitle=None, categories=None,
            feed_url=None, feed_copyright=None, feed_guid=None, ttl=None, **kwargs):
            
        Atom1Feed.__init__(self, title, link, description, language, author_email,
            author_name, author_link, subtitle, categories,
            feed_url, feed_copyright, feed_guid, ttl, **kwargs)
            
        self.feed['total'] = total
        self.feed['items_per_page'] = items_per_page
        self.feed['item_language'] = item_language
                
#        print self.feed
        
    def add_root_elements(self, handler):
        Atom1Feed.add_root_elements(self, handler)
        handler.addQuickElement(u"opensearch:totalResults", self.feed['total'])
        handler.addQuickElement(u"opensearch:itemsPerPage", self.feed['items_per_page'])
    
    def add_item_elements(self, handler, item):
        Atom1Feed.add_item_elements(self, handler, item)
        handler.addQuickElement(u"dcterms:language", self.feed['item_language'])
#        handler.addQuickElement(u"ent", self.feed['zxc'])        

    def write(self, outfile, encoding):
        handler = SimplerXMLGenerator(outfile, encoding)
        handler.startDocument()
        handler.startElement(u'feed', self.root_attributes())
        self.add_root_elements(handler)
        self.write_items(handler)
        handler.endElement(u"feed")
        
    def write_items(self, handler):
        for item in self.items:
            handler.startElement(u"entry", self.item_attributes(item))
            self.add_item_elements(handler, item)
            handler.endElement(u"entry")


        
class BookFeed(Feed):
    
    def __init__(self, slug, request):
        Feed.__init__(self, slug, request)
        self.title = "search result for: " + slug
            
    feed_type = OPDSFeed    
    total = "%s" % len(Book.objects.all())
    items_per_page = "20"
    author = "SITE"
#    subtitle = "subtitle"
    link = "LINK FOR SEARCHING"
    description = "description"
    item_language = Book.objects.all()[1].lang

    
    def get_absolute_url():
    	return "get absolute URL"
    	
    def item_link(self):
    	return "item-link"

    def items(self):
        return Book.objects.all()
        
    def feed_extra_kwargs(self, obj):
        return {'total': self.total, 'items_per_page': self.items_per_page, 'item_language': self.item_language, }

#    def item_extra_kwargs(self, item):
#        return {'ent': self.ent, }

