# train classifier on feedbooks.com

import re
import spec.external.feedparser as feedparser
import classifier
import pickle

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

            hyp = classifier.classify(fulltext)
            print 'Hypothesis: ' + str(hyp)
                        
            for cat in entry['categories']:
                c = cat[1].encode('utf-8')
                print ' Tag : ' + c
                classifier.train(fulltext, c)
                
                if c == hyp[0] or c == hyp[1]:
                    write_statistics(c)
        
def write_statistics(tag_name):
    file_handle = open("statistics", "a")
    file_handle.write(tag_name)
    file_handle.write('\n')    
    file_handle.close()
    
def get_statistics():
    file_handle = open("statistics", "r")
    lines = file_handle.readlines()
    lines.sort()
    
    cat = list()
    w = None
    
    for l in lines:
        if l != w:
            cat.append(l)
            w = l
    
    total = 0
    for l in cat:
        count = lines.count(l)
        total += count
        print l, count
    
    print "Total: ", total
