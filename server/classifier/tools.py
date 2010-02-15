# train classifier on feedbooks.com

import re
import spec.external.feedparser as feedparser
import spec.external.pyPdf as pyPdf
import classifier
import urllib

def read(feed, classifier):
    ''' gets URL and classify items '''
    # get feed items
    f = feedparser.parse(feed)
#    file_handle = open("statistics", "a")
    
    for entry in f['entries']:
        summary = entry['summary'].encode('utf-8')
        if summary == "No description available.":
            break
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
            
            # write statistic
#                if c == hyp[0] or c == hyp[1]:     
#                    file_handle.write(tag_name)
#                    file_handle.write('\n')    

#    file_handle.close()
        
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
    
def classify_fullbook(feed, classif):
    f = feedparser.parse(feed)
    
    for entry in f['entries']:
        url = entry['id'].encode('utf-8') + ".pdf"
        webFile = urllib.urlopen(url)
        localFile = open("downloads/" + url.split('/')[-1], 'w')
        localFile.write(webFile.read())
        fulltext = webFile.read()
        webFile.close()
        localFile.close()

        content = ""
        # Load PDF into pyPDF
        pdf = pyPdf.PdfFileReader(file("downloads/" + url.split('/')[-1], "rb"))
        # Iterate pages
        for i in range(0, pdf.getNumPages()):
            # Extract text from page and add to content
            content += pdf.getPage(i).extractText() + "\n"
        # Collapse whitespace
        content = " ".join(content.replace(u"\xa0", " ").strip().split())

        print 
        print '-----'


        #hyp = classif.classify(fulltext)
        #print 'Hypothesis: ' + str(hyp)
                    
        #for cat in entry['categories']:
        #    c = cat[1].encode('utf-8')
        #    print ' Tag : ' + c
        #    classif.train(fulltext, c)
           
