''' some tools for book classifier  '''
from book.models import Book, Tag, Annotation

import pickle
from read_tools import get_description, read
from spec.external.BeautifulSoup import BeautifulSoup as bs
import urllib2
import datetime
from search_tools import GENRES

def get_statistics():
    ''' parse statistic file and print statistics for classifier'''
    
    file_handle = open("statistics", "r")
    lines = file_handle.readlines()
    lines.sort()
    
    cathegories = list()
    prev = None
    
    for line in lines:
        if line != prev:
            cathegories.append(line)
            prev = line
    
    total = 0
    for cat in cathegories:
        count = lines.count(cat)
        total += count
        print cat, count
    
    print "Total: ", total
    
def check_classifier(classif):
    ''' 
    first - train classifier, then - try to classify 50 pages 
    from feedbooks.com
    '''
    
    classif.sample_train()
    
    counter = 0

    #check on www.allromanceebooks.com
#    for i in xrange(10, 15):
#        counter += read_all_romance(True, ('http://www.allromanceebooks.com/epub-feed.xml?search=recent+additions;page=%s' % i), classif)
        
    # check on feedbooks
    for i in xrange(10, 15):
        counter += read(True, 
        ('http://feedbooks.com/books.atom?lang=en&amp;page=%s' % i), classif)

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
    ''' deserialize classifier from file "classifier.ser" '''
    file_handle = open("classifier.ser", "r")
    pickle_load = pickle.load(file_handle)
    file_handle.close()
    return pickle_load

def get_beaut_soup(url):
    ''' reads page and return it's beaut_soup'''
    opener = urllib2.build_opener()
    opener.addheaders = [('User-agent', 'Mozilla/5.0')]    
    infile = opener.open(url)
    page = infile.read()
    return bs.BeautifulSoup(page)
