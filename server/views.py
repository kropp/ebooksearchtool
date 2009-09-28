from django.contrib.syndication.feeds import Feed
from model.models import Publisher
from django.utils.feedgenerator import SyndicationFeed
from django.utils.feedgenerator import Rss201rev2Feed
from django.utils.xmlutils import SimplerXMLGenerator
from django.utils.encoding import force_unicode, iri_to_uri
import re
import datetime
from django.utils.feedgenerator import rfc3339_date

class OPDSFeed(Atom1Feed ):
    mime_type = 'application/atom+xml'
    ns = u"http://www.w3.org/2005/Atom"
    def __init__(self, title, link, description, title3 = None, language=None, author_email=None,
        author_name=None, author_link=None, subtitle=None, categories=None,
        feed_url=None, feed_copyright=None, feed_guid=None, ttl=None, **kwargs):
        to_unicode = lambda s: force_unicode(s, strings_only=True)
        if categories:
            categories = [force_unicode(c) for c in categories]             

        self.feed = {
            'title': to_unicode(title),
            'title3': to_unicode(title3),
            'link': iri_to_uri(link),
            'description': to_unicode(description),
            'language': to_unicode(language),
            'author_email': to_unicode(author_email),
            'author_name': to_unicode(author_name),
            'author_link': iri_to_uri(author_link),
            'subtitle': to_unicode(subtitle),
            'categories': categories or (),
            'feed_url': iri_to_uri(feed_url),
            'feed_copyright': to_unicode(feed_copyright),
            'id': feed_guid or link,
            'ttl': ttl,
            }
        self.feed.update(kwargs)
        self.items = []

    def write(self, outfile, encoding):
        handler = SimplerXMLGenerator(outfile, encoding)
        handler.startDocument()
        handler.startElement(u'feed', self.root_attributes())
        self.add_root_elements(handler)
        self.write_items(handler)
        handler.endElement(u"feed")
         	
    def root_attributes(self):
        if self.feed['language'] is not None:
            return {u"xmlns": self.ns, u"xml:lang": self.feed['language']}
        else:
            return {u"xmlns": self.ns}

    def add_root_elements(self, handler):
 	        handler.addQuickElement(u"title", self.feed['title'])
 	        handler.addQuickElement(u"link", "", {u"rel": u"alternate", u"href": self.feed['link']})
 	        if self.feed['feed_url'] is not None:
 	            handler.addQuickElement(u"link", "", {u"rel": u"self", u"href": self.feed['feed_url']})
 	        handler.addQuickElement(u"id", self.feed['id'])
 	        handler.addQuickElement(u"updated", rfc3339_date(self.latest_post_date()).decode('utf-8'))
 	        handler.addQuickElement(u"title3", self.feed['title3'])
 	        if self.feed['author_name'] is not None:
 	            handler.startElement(u"author", {})
 	            handler.addQuickElement(u"name", self.feed['author_name'])
 	            if self.feed['author_email'] is not None:
 	                handler.addQuickElement(u"email", self.feed['author_email'])
 	            if self.feed['author_link'] is not None:
 	                handler.addQuickElement(u"uri", self.feed['author_link'])
 	            handler.endElement(u"author")
 	        if self.feed['subtitle'] is not None:
 	            handler.addQuickElement(u"subtitle", self.feed['subtitle'])
 	        for cat in self.feed['categories']:
 	            handler.addQuickElement(u"category", "", {u"term": cat})
 	        if self.feed['feed_copyright'] is not None:
 	            handler.addQuickElement(u"rights", self.feed['feed_copyright'])
 	
    def write_items(self, handler):
        for item in self.items:
            handler.startElement(u"entry", self.item_attributes(item))
            self.add_item_elements(handler, item)
            handler.endElement(u"entry")
 	
    def add_item_elements(self, handler, item):
        handler.addQuickElement(u"title", item['title'])
#        print item
#        handler.addQuickElement(u"title2", item['title2'])
        handler.addQuickElement(u"link", u"", {u"href": item['link'], u"rel": u"alternate"})
        if item['pubdate'] is not None:
            handler.addQuickElement(u"updated", rfc3339_date(item['pubdate']).decode('utf-8'))
            handler.addQuickElement(u"title3", item['title3'])
        # Author information.
        if item['author_name'] is not None:
            handler.startElement(u"author", {})
            handler.addQuickElement(u"name", item['author_name'])
            if item['author_email'] is not None:
                handler.addQuickElement(u"email", item['author_email'])
            if item['author_link'] is not None:
                handler.addQuickElement(u"uri", item['author_link'])
            handler.endElement(u"author")

        # Unique ID.
        if item['unique_id'] is not None:
            unique_id = item['unique_id']
        else:
            unique_id = get_tag_uri(item['link'], item['pubdate'])
        handler.addQuickElement(u"id", unique_id)

        # Summary.
        if item['description'] is not None:
            handler.addQuickElement(u"summary", item['description'], {u"type": u"html"})

        # Enclosure.
        if item['enclosure'] is not None:
            handler.addQuickElement(u"link", '',
                {u"rel": u"enclosure",
                 u"href": item['enclosure'].url,
                 u"length": item['enclosure'].length,
                 u"type": item['enclosure'].mime_type})

        # Categories.
        for cat in item['categories']:
            handler.addQuickElement(u"category", u"", {u"term": cat})

        # Rights.
        if item['item_copyright'] is not None:
            handler.addQuickElement(u"rights", item['item_copyright'])
 	            
class LatestEntries(Feed):

    feed_type = OPDSFeed

    title = "title"
    language = "language"
    title3 = "title3"
    link = "link"
    description = "description"
    
    def get_absolute_url():
    	return "get absolute URL"
    	
    def item_link(self):
    	return "http://www.apress.com/"

    def items(self):
        return Publisher.objects.all()



