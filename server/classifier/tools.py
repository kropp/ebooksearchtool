# train classifier on feedbooks.com

import re
import spec.external.feedparser as feedparser
import classifier
import urllib

from spec.external.pdfminer.pdfinterp import PDFResourceManager, process_pdf
from spec.external.pdfminer.converter import TextConverter
from spec.external.pdfminer.layout import LAParams
from spec.external.pdfminer.pdfparser import PDFDocument, PDFParser

from spec.external.pdfminer.pdftypes import stream_value, PDFStream, PDFObjRef
from spec.external.pdfminer.psparser import PSKeyword
#from pdfminer.pdftypes import PDFStream, PDFObjRef, resolve1, stream_value

ESC_PAT = re.compile(r'[\000-\037&<>()\042\047\134\177-\377]')
def esc(s):
    return ESC_PAT.sub(lambda m:'&#%d;' % ord(m.group(0)), s)

def read(b, feed, classif):
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

        hyp = classif.classify(fulltext)
#        print 'Hypothesis: ' + str(hyp)
                    
        for cat in entry['categories']:
            c = cat[1].encode('utf-8')
#            print ' Tag : ' + c
            if b == False:
                classif.train(fulltext, c)
            
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
       
def read_fullbook(b, feed, classif):
    f = feedparser.parse(feed)
    
    counter = 0
    if b == True:
        file_handle = open("statistics", "a")
    
    for entry in f['entries']:
        url = entry['id'].encode('utf-8') + ".pdf"
        webFile = urllib.urlopen(url)
        localFile = open("downloads/" + url.split('/')[-1], 'w')
        localFile.write(webFile.read())
        fulltext = webFile.read()
        webFile.close()
        localFile.close()

        content = ""
        # parse pdf
        filename = "downloads/" + url.split('/')[-1]
        read_pdf(filename)
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
                file_handle.write(c)
                file_handle.write('\n')    
                file_handle.flush()

#    if b == True:
#        file_handle.close()
    
    return counter

def check_fullclassifier(classif):
    classif.sample_full_train()
    
    counter = 0
    
    for i in range(10, 15):
        counter += read_fullbook(True, ('http://feedbooks.com/books.atom?lang=en&amp;page=%s' % i), classif)
        
    print "Total: ", counter
    return

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
    
def dump_pdf(input_file, pages):
    password = ''
    doc = PDFDocument()
    fp = file(input_file, 'rb')
    parser = PDFParser(fp)
    parser.set_document(doc)
    doc.set_parser(parser)
    doc.initialize(password)
    pagenos = set()
    pagenos.update( int(x)-1 for x in pages.split(',') )
    codec = 'text'
    outfile = input_file[0:-4] + ".txt"
    outfp = file(outfile, 'w')

    if pagenos:
        for (pageno,page) in enumerate(doc.get_pages()):
            if pageno in pagenos:
                if codec:
                    for obj in page.contents:
                        obj = stream_value(obj)
                        dumpxml(outfp, obj, codec=codec)
                else:
                    dumpxml(outfp, page.attrs)

    outfp.write('\n')
    return
    
def dumpxml(out, obj, codec=None):
    if isinstance(obj, dict):
        out.write('<dict size="%d">\n' % len(obj))
        for (k,v) in obj.iteritems():
            out.write('<key>%s</key>\n' % k)
            out.write('<value>')
            dumpxml(out, v)
            out.write('</value>\n')
        out.write('</dict>')
        return

    if isinstance(obj, list):
        out.write('<list size="%d">\n' % len(obj))
        for v in obj:
            dumpxml(out, v)
            out.write('\n')
        out.write('</list>')
        return

    if isinstance(obj, str):
        out.write('<string size="%d">%s</string>' % (len(obj), esc(obj)))
        return

    if isinstance(obj, PDFStream):
        if codec == 'raw':
            out.write(obj.get_rawdata())
        elif codec == 'binary':
            out.write(obj.get_data())
        else:
            out.write('<stream>\n<props>\n')
            dumpxml(out, obj.attrs)
            out.write('\n</props>\n')
            if codec == 'text':
                data = obj.get_data()
                out.write('<data size="%d">%s</data>\n' % (len(data), esc(data)))
            out.write('</stream>')
        return

    if isinstance(obj, PDFObjRef):
        out.write('<ref id="%d"/>' % obj.objid)
        return

    if isinstance(obj, PSKeyword):
        out.write('<keyword>%s</keyword>' % obj.name)
        return

    if isinstance(obj, PSLiteral):
        out.write('<literal>%s</literal>' % obj.name)
        return

    if isinstance(obj, int) or isinstance(obj, float):
        out.write('<number>%s</number>' % obj)
        return

    raise TypeError(obj)
