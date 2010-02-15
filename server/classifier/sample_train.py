# train classifier on feedbooks.com

import re
import spec.external.feedparser as feedparser
import classifier

def read(feed, classifier):
    ''' gets URL and classify items '''
    # get feed items
    f = feedparser.parse(feed)
    
    for entry in f['entries']:
        summary = entry['summary'].encode('utf-8')
        if summary != "No description available.":
            print 
            print '-----'
#            print 'Title: ' + entry['title'].encode('utf-8')
#            print 'Author: ' + entry['author'].encode('utf-8')
#            print
#            print summary
            
            fulltext = '%s\n%s' % (entry['title'].encode('utf-8'), summary)

            print 'Hypothesis: ' + str( classifier.classify(fulltext))
                        
            for cat in entry['categories']:
                print ' Tag : ' + cat[1].encode('utf-8')
                classifier.train(fulltext, cat[1].encode('utf-8'))


def sample_train(classifier):
    for i in range(1, 150):
        read(('http://feedbooks.com/books.atom?lang=en&amp;page=%s'% i), classifier)
        
        
