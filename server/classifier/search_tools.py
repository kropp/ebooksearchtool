''' Package for search additional information for our model '''

def search_for_author_information(author_name):     #TODO
    ''' search information about author in wiki '''
    auth_url = "http://en.wikipedia.org/wiki/" + author_name    
    opener = urllib2.build_opener()
    opener.addheaders = [('User-agent', 'Mozilla/5.0')]
    infile = opener.open(auth_url)
    page = infile.read()

    beaut_soup = bs.BeautifulSoup(page)
    text = beaut_soup.getText()  
