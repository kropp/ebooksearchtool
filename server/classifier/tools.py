# train classifier on feedbooks.com

import re
import spec.external.feedparser as feedparser
import classifier
import urllib2

from spec.external.pdfminer.pdfinterp import PDFResourceManager, process_pdf
from spec.external.pdfminer.converter import TextConverter
from spec.external.pdfminer.layout import LAParams
from spec.external.pdfminer.pdfparser import PDFDocument, PDFParser

from spec.external.BeautifulSoup import BeautifulSoup as bs

import random

def read(b, feed, classif):
    ''' gets URL and classify items '''
    # get feed items
    f = feedparser.parse(feed)

    counter = 0
    if b == True:
        file_handle = open("statistics", "a")
    
    for entry in f['entries']:
        summary = entry['summary'].encode('utf-8')
        
        len_sum = len(summary.split())
        if len_sum < 10:
            summary = get_description(entry['title'].encode('utf-8'))
            if summary.__str__() == "None":
                break

        # find author and his tags
#        author_ref = entry['author_detail']['href'].encode('utf-8')
#        auth_parser = feedparser.parse(author_ref)

        counter = counter+1
       
#        print entry['title'].encode('utf-8')
#        fulltext = '%s\n%s' % (entry['title'].encode('utf-8'), summary)

        fulltext = summary
        
        if entry.get('dcterms_language') == "ru":
            hyp = classif.classify(fulltext, lang="russian")
        else:
            hyp = classif.classify(fulltext)
            
#        print 'Hypothesis: ' + str(hyp)
                    
        for cat in entry.get('categories'):
            c = cat[1].encode('utf-8')
#            print ' Tag : ' + c
            if b == False:
                if entry.get('dcterms_language') == "ru":
                    classif.train(fulltext, c, "russian")
                else:
                    classif.train(fulltext, c)
            
            # write statistic

            if b == True and (c == hyp[0] or c == hyp[1]):     
                file_handle.write(c)
                file_handle.write('\n')    

    if b == True:
        file_handle.close()
                
    return counter
        
def get_statistics():
    ''' parse statistic file and print statistics for classifier'''
    
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
    
def check_classifier(classif):
    ''' first - train classifier, then - try to classify 50 pages from feedbooks.com'''
    
    classif.sample_train()
    
    counter = 0
    
    for i in range(10, 15):
        counter += read(True, ('http://feedbooks.com/books.atom?lang=en&amp;page=%s' % i), classif)
        
    print "Total: ", counter
    
def get_description(book_name):
    ''' gets book description from Amazon.com'''
    
    name = book_name.replace(" ", "+")
#    print name
    name = urllib2.quote(name)
    
    page = urllib2.urlopen("http://www.amazon.com/s/ref=nb_sb_noss?url=search-alias%3Dstripbooks&field-keywords=" + "%s&x=0&y=0" % name)
    beaut_soup = bs.BeautifulSoup(page)
    book_tag = ""
    book_tag = beaut_soup.find(attrs={"class":"productTitle"})
    if book_tag.__str__() == "None":
        return
    book_html = book_tag.find("a")["href"]

    book_page = urllib2.urlopen(book_html)
    beaut_soup_book = bs.BeautifulSoup(book_page)
    if beaut_soup_book.__str__() == "None":
        return
    descr_tag = beaut_soup_book.find(attrs={"class": "productDescriptionWrapper"})
    if descr_tag.__str__() == "None":
        return
    description = descr_tag.getText()
    page.close()
    return description
    
    
# next functions are not used now       
def read_fullbook(b, feed, classif):
    ''' read function for full text of book'''
    
    f = feedparser.parse(feed)
    
    counter = 0
    if b == True:
        file_handle_stat = open("statistics", "a")
    
    for entry in f['entries']:
        counter = counter+1
        url = entry['id'].encode('utf-8') + ".pdf"
        webFile = urllib2.urlopen(url)
        localFile = open("downloads/" + url.split('/')[-1], 'w')
        localFile.write(webFile.read())
        fulltext = webFile.read()
        webFile.close()
        localFile.close()

        content = ""
        # parse pdf
        filename = "downloads/" + url.split('/')[-1]
        
        pages = set()
        
        doc = PDFDocument()
        fp = file(filename, 'rb')
        parser = PDFParser(fp)
        parser.set_document(doc)
        doc.set_parser(parser)
        doc.initialize('')
        
        count_page = []
        for (pageno,page) in enumerate(doc.get_pages()):
            count_page.append(pageno)
            
        p_count = len(count_page)
        
        for i in range(1, 10):
            pages.add(random.randrange(4, p_count - 3))
        
        read_pdf(filename, pages)
        outputfile = filename[0:-4] + ".txt"
        
        content = []
        file_handle = open(outputfile, "r")
        content = file_handle.readlines()
        file_handle.close()

        fulltext = ''
        for i in content:
            fulltext += i
        #content = " ".join(content.replace(u"\xa0", " ").strip().split())

#        fulltext = " ".join(fulltext.replace(u"\xa0", " ").strip().split())
    #    return fulltext
        hyp = classif.classify(fulltext)
        print 'Hypothesis: ' + str(hyp)
                
        for cat in entry['categories']:
            c = cat[1].encode('utf-8')
            print c

            if b == False:
                classif.train(fulltext, c)
                
            if b == True and (c == hyp[0] or c == hyp[1]):     
                file_handle_stat.write(c)
                file_handle_stat.write('\n')    
                file_handle_stat.flush()

    if b == True:
        file_handle_stat.close()
    
    return counter

def check_fullclassifier(classif):
    ''' check function for full text classifier'''

    classif.sample_full_train()
    
    counter = 0
    
    for i in range(10, 15):
        counter += read_fullbook(True, ('http://feedbooks.com/books.atom?lang=en&amp;page=%s' % i), classif)
        
    print "Total: ", counter
    return

def read_pdf(input_file, pages):
    ''' reads pdf file, extract information'''

    password = ''
    pagenos = set()
    maxpages = 0
    # output option
    outfile = input_file[0:-4] + ".txt"
    outtype = 'text'
    codec = 'utf-8'
    laparams = LAParams()
    pagenos.update( int(x)-1 for x in pages )

    rsrc = PDFResourceManager()

    outfp = file(outfile, 'w')

    device = TextConverter(rsrc, outfp, codec=codec, laparams=laparams)
 
    fp = file(input_file, 'rb')
    process_pdf(rsrc, device, fp, pagenos, maxpages=maxpages, password=password)
    fp.close()
    device.close()
    outfp.close()
    return
    

