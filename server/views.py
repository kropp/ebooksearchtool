from django.contrib.syndication.feeds import Feed
from books.models import Book
from django.utils.feedgenerator import Atom1Feed

class OPDSFeed(Atom1Feed ):
    "Feed class to support OPDS protocol"
    
    def __init__(self, title, link, description, tit=None, language=None, author_email=None,
            author_name=None, author_link=None, subtitle=None, categories=None,
            feed_url=None, feed_copyright=None, feed_guid=None, ttl=None, **kwargs):
            
        Atom1Feed.__init__(self, title, link, description, language, author_email,
            author_name, author_link, subtitle, categories,
            feed_url, feed_copyright, feed_guid, ttl, **kwargs)
            
        self.feed['tit'] = tit
                
#        print self.feed
        
    def add_root_elements(self, handler):
        Atom1Feed.add_root_elements(self, handler)
        handler.addQuickElement(u"tit", self.feed['tit'])
    
    def add_item_elements(self, handler, item):
        Atom1Feed.add_item_elements(self, handler, item)
        handler.addQuickElement(u"ent", self.feed['ent'])
#        handler.addQuickElement(u"ent", self.feed['zxc'])        
        
class BookFeed(Feed):

    feed_type = OPDSFeed
    tit = "titimpl"
    title = "RESULT FOR SEARCH"
    author = "SITE"
    subtitle = "subtitle"
    language = "language"
    link = "LINK FOR SEARCHING"
    description = "description"
    ent = "ent"
    
    def get_absolute_url():
    	return "get absolute URL"
    	
    def item_link(self):
    	return "item-link"

    def items(self):
        return Book.objects.all()
        
    def feed_extra_kwargs(self, obj):
        return {'tit': self.tit, 'ent': self.ent, }

#    def item_extra_kwargs(self, item):
#        return {'ent': self.ent, }

