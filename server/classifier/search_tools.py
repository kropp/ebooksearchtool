''' Package for search additional information for our model '''

import urllib2
from spec.external.BeautifulSoup import BeautifulSoup as bs

genres = {'Adventure':['adventure'], 'Biography':['biography'], 'Collections':['collections'],
    'Crime/Mystery':['crime/mystery','crime', 'mystery'], 'Essay':['essay'], 
    'Fantasy':['fantasy'], 'Ghost Stories':['ghost stories', 'ghost-stories'], 'History':['history'],
    'Horror':['horror'], 'Humor/Satire':['humor/satire','humor','satire', 'comedy', 'black comedy'],
    'Non-Fiction':['non-fiction', 'non fiction'], 'Novels':['novels','novelist'], 
    'Philosophy':['philosophy'], 'Plays':['plays'], 'Poetry':['poetry', 'poet'],
    'Politics':['politics'], 'Religion':['religion'], 'Romance':['romance'],
    'Science':['science'], 'Science Fiction':['science fiction', 'science-fiction'],
    'Sexuality':['sexuality'], 'Short Fiction':['short fiction', 'short-fiction'], 'Thriller':['thriller'],
    'Travel':['travel'], 'War':['war'], 'Western':['western'], 
    'Young Readers':['young readers', 'young-readers']}

# all genres can be used with writer

def search_for_author_information(author):     
    ''' search information about author in wiki '''

    auth_url = "http://en.wikipedia.org/wiki/" + author.name
    opener = urllib2.build_opener()
    opener.addheaders = [('User-agent', 'Mozilla/5.0')]
    infile = opener.open(auth_url)
    page = infile.read()

    beaut_soup = bs.BeautifulSoup(page)
    text = beaut_soup.getText()

    is_article = text.find("Wikipedia does not have an article with this exact name")
    if is_article != -1:
        auth_url = "http://en.wikipedia.org/wiki/Special:Search/" + author.name
        infile = opener.open(auth_url)
        page = infile.read()
        beaut_soup = bs.BeautifulSoup(page)
        did_you = beaut_soup.find(attrs={'class':'searchdidyoumean'})
        
        if did_you != None:
            auth_url = "http://en.wikipedia.org" + did_you
            infile = opener.open(auth_url)
            page = infile.read()
            beaut_soup = bs.BeautifulSoup(page)
            text = beaut_soup.getText()
            is_result = text.find('Search results')
            if is_result != -1:
                first_res = beaut_soup.find(attrs={'class':'mw-search-results'})
                article = first_res.find('li')
                text = article.getText().lower()
                is_writer = text.find('writer')
                if is_writer == -1:
                    return
                else:
                    auth_url = article.find('a')['href']
                    infile = opener.open(auth_url)
                    page = infile.read()
                    beaut_soup = bs.BeautifulSoup(page)
###############
    text = beaut_soup.getText()
    text = text.lower()

    for i in genres.items():
        for j in i[1]:
            k = text.find(j)
            if k != -1:
                t = Tag.objects.get_or_create(i[0])
                author.tag.add(t[0])


