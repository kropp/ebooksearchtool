# train classifier on feedbooks.com

import re
import spec.external.feedparser as feedparser
import spec.external.pyPdf as pyPdf
import classifier
import urllib

from spec.external.pdfminer.pdfinterp import PDFResourceManager, process_pdf
from spec.external.pdfminer.converter import TextConverter
from spec.external.pdfminer.layout import LAParams

def read(b, feed, classifier):
    ''' gets URL and classify items '''
    # get feed items
    f = feedparser.parse(feed)

    counter = 0
    if b == True:
        file_handle = open("statistics", "a")
    
    for entry in f['entries']:
        summary = entry['summary'].encode('utf-8')
        if summary == "No description available.":
            break

        counter = counter+1
       
        fulltext = '%s\n%s' % (entry['title'].encode('utf-8'), summary)

        hyp = classifier.classify(fulltext)
#        print 'Hypothesis: ' + str(hyp)
                    
        for cat in entry['categories']:
            c = cat[1].encode('utf-8')
#            print ' Tag : ' + c
            if b == False:
                classifier.train(fulltext, c)
            
            # write statistic

            if b == True and (c == hyp[0] or c == hyp[1]):     
                file_handle.write(c)
                file_handle.write('\n')    

    if b == True:
        file_handle.close()
                
    return counter
        
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
    
def check_classifier(classif):
    classif.sample_train()
    
    counter = 0
    
    for i in range(100, 150):
        counter += read(True, ('http://feedbooks.com/books.atom?lang=en&amp;page=%s' % i), classif)
        
    print "Total: ", counter
       
def read_fullbook(feed, classif):
    f = feedparser.parse(feed)
    
#    for entry in f['entries']:
    entry = f['entries'][0]
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
    print content

    #hyp = classif.classify(fulltext)
    #print 'Hypothesis: ' + str(hyp)
                
    #for cat in entry['categories']:
    #    c = cat[1].encode('utf-8')
    #    print ' Tag : ' + c
    #    classif.train(fulltext, c)       

def read_pdf(input_file):

    password = ''
    pagenos = set()
    maxpages = 0
    # output option
    outfile = input_file[0:-4] + ".txt"
    outtype = 'text'
    codec = 'utf-8'
    laparams = LAParams()

    rsrc = PDFResourceManager()

    outfp = file(outfile, 'w')

    device = TextConverter(rsrc, outfp, codec=codec, laparams=laparams)
 
    fp = file(input_file, 'rb')
    process_pdf(rsrc, device, fp, pagenos, maxpages=maxpages, password=password)
    fp.close()
    device.close()
    outfp.close()
    return
