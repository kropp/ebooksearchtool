''' module for find information about authors '''

import urllib2

from tools import get_beaut_soup

def classify_authors():
    ''' goes throw links list and tries to find 
        authors in our data base '''
#    genres_list = get_genres_list()
#    for link in genres_list:
    list_url = "http://en.wikipedia.org/wiki/List_of_biographers"
    beaut_soup = get_beaut_soup(list_url)
    authors_link_list = beaut_soup.find(attrs={'id': 
                    'Some_notable_authors_of_biographies'}).findNext().\
                                                                findAll('li')
    authors_list = list()
    for author in authors_link_list:
        auth_link = author.find('a')
        if auth_link:
            author = auth_link['href'].split('/')[2].replace('_', ' ')
            if author.find("(") == -1:
                authors_list.append()
    return authors_list

