''' Package for full-text classification '''

from spec.external.pdfminer.pdfinterp import PDFResourceManager, process_pdf
from spec.external.pdfminer.converter import TextConverter
from spec.external.pdfminer.layout import LAParams
from spec.external.pdfminer.pdfparser import PDFDocument, PDFParser

from book.models import Author, Book, Language, Annotation, Tag, BookFile

import random

import urllib2
import zipfile
from spec.external.BeautifulSoup import BeautifulSoup as bs

# next functions are not used right now       
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
        
        for i in xrange(1, 10):
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
    
    for i in xrange(10, 15):
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
    
    
