''' Package for search additional information for our model '''

import urllib2
from spec.external.BeautifulSoup import BeautifulSoup as bs

genres = {'Adventure':['Adventure', 'adventure novel'], 'Biography':['Biography'], 'Collections':['Collections'],
    'Crime/Mystery':['Crime/Mystery','Crime', 'Mystery'], 'Essay':['Essay'], 
    'Fantasy':['Fantasy'], 'Ghost Stories':['Ghost Stories'], 'History':['History'],
    'Horror':['Horror'], 'Humor/Satire':['Humor/Satire','Humor','Satire', 'Comedy', 'Black comedy'],
    'Non-Fiction':['Non-Fiction'], 'Novels':['Novels','Novelist', 'adventure novel'], 
    'Philosophy':['Philosophy'], 'Plays':['Plays'], 'Poetry':['Poetry', 'poet'],
    'Politics':['Politics'], 'Religion':['Religion'], 'Romance':['Romance'],
    'Science':['Science'], 'Science Fiction':['Science Fiction'],
    'Sexuality':['Sexuality'], 'Short Fiction':['Short Fiction'], 'Thriller':['Thriller'],
    'Travel':['Travel'], 'War':['War'], 'Western':['Western'], 
    'Young Readers':['Young Readers']}

# all genres can be used with writer

def search_for_author_information(author_name):     #TODO
    ''' search information about author in wiki '''

    auth_url = "http://en.wikipedia.org/wiki/" + author_name    
    opener = urllib2.build_opener()
    opener.addheaders = [('User-agent', 'Mozilla/5.0')]
    infile = opener.open(auth_url)
    page = infile.read()

    beaut_soup = bs.BeautifulSoup(page)
    text = beaut_soup.getText()

