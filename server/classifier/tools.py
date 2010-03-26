# train classifier on feedbooks.com

from book.models import Book, Tag, Annotation

import pickle
from read_tools import *

def tag_adding():
    classifier = deserialize()
    books = Book.objects.all()
    counter = 0
    for book in books:
        if book.tag.all() != None:
            continue
        counter +=1
        ann = book.annotation.all()
        summary = ""
        authors = book.author_set.all()
        if ann.count() == 0:
            if authors.count() != 0:
                summary = get_description(book.title + authors[0].name, True)
            else:
                summary = get_description(book.title, True)
            if summary == None:
                continue
            ann = Annotation.objects.get_or_create(name=summary)        
            book.annotation.add(ann[0])
        else:
            for i in ann:
                summary += i.name
                     
            len_sum = len(summary.split())
            if len_sum < 50:
                if authors.count() != 0:
                    summary = get_description(book.title + authors[0].name, True)
                else:
                    summary = get_description(book.title, True)
            if summary == None:
                continue
            ann = Annotation.objects.get_or_create(name=summary)        
            book.annotation.add(ann[0])
       
        tags = classifier.classify(summary)
        if counter % 10 == 0:
            print counter
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

