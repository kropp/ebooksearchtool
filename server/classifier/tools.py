# train classifier on feedbooks.com

import re
import urllib2

from spec.external.BeautifulSoup import BeautifulSoup as bs

from book.models import Language, Book, Tag

import pickle
from read_tools import *
import classifier

def tag_adding():
    classifier = deserialize()
    books = Book.objects.all()
    
    for book in books:
        ann = book.annotation.all()
        summary = ""
        if ann.count() == 0:
            summary = get_description(book.title)
            if summary == None:
                continue
        else:
            for i in ann:
                summary += i.name
                     
            len_sum = len(summary.split())
            if len_sum < 10:
                summary = get_description(book.title)
            if summary == None:
                continue
                
        tags = classifier.classify(summary)
        
        if tags[0] != None:
            t = Tag.objects.get_or_create(name=tags[0])
            book.tag.add(t[0])
        if tags[1] != None:
            t = Tag.objects.get_or_create(name=tags[1])
            book.tag.add(t[0])
        
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

    #check on www.allromanceebooks.com
#    for i in xrange(10, 15):
#        counter += read_all_romance(True, ('http://www.allromanceebooks.com/epub-feed.xml?search=recent+additions;page=%s' % i), classif)
        
    # check on feedbooks
    for i in xrange(10, 15):
        counter += read(True, ('http://feedbooks.com/books.atom?lang=en&amp;page=%s' % i), classif)

    # check on smashwords
#    for i in xrange(10, 15):
#        counter += read_smashwords(True, ('http://www.smashwords.com/atom/books/1/popular/epub/any/0?page=%s' % i), classif)
        
    print "Total: ", counter
    
def get_description(book_name):
    ''' gets book description from Amazon.com'''
    
    name = book_name.replace(" ", "+")
#    print name
    name = name.encode("utf-8")
    name = urllib2.quote(name)
    
    page = urllib2.urlopen("http://www.amazon.com/s/ref=nb_sb_noss?url=search-alias%3Dstripbooks&field-keywords=" + "%s&x=0&y=0" % name)
    beaut_soup = bs.BeautifulSoup(page)
    book_tag = ""
    book_tag = beaut_soup.find(attrs={"class":"productTitle"})
    if book_tag == None:
        return
    book_html = book_tag.find("a")
    if book_html == None:
        return
    book_html = book_html.get("href")
    

    book_page = urllib2.urlopen(book_html)
    beaut_soup_book = bs.BeautifulSoup(book_page)
    if beaut_soup_book == None:
        return
    descr_tag = beaut_soup_book.find(attrs={"class": "productDescriptionWrapper"})
    if descr_tag == None:
        return
    description = descr_tag.getText()
    page.close()
    return description
    

def serialize(classifier):
    ''' serialize classifier to classifier.ser'''
    file_handle = open("classifier.ser", "a")
    pickle.dump(classifier, file_handle)
    file_handle.close()
    
def deserialize():
    file_handle = open("classifier.ser", "r")
    cl = pickle.load(file_handle)
    file_handle.close()
    return cl



