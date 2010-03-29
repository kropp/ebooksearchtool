''' Package for search additional information for our model '''

import urllib2
from spec.external.BeautifulSoup import BeautifulSoup as bs
from book.models import Tag

genres = {'Adventure':['Adventure'], 'Biography':['Biography'], 'Collections':['Collections'],
    'Crime/Mystery':['Crime/Mystery','Crime', 'Mystery'], 'Essay':['Essay'], 
    'Fantasy':['Fantasy'], 'Ghost Stories':['Ghost Stories', 'Ghost-Stories'], 'History':['Historian'],
    'Horror':['Horror'], 'Humor/Satire':['Humor/Satire','Humor','Satire', 'Comedy', 'Black Comedy'],
    'Non-Fiction':['Hon-Fiction', 'Non Fiction'], 'Novels':['Novels','Novelist'], 
    'Philosophy':['Philosophy'], 'Plays':['Plays', 'Play', 'Playwright'], 'Poetry':['Poetry', 'Poet'],
    'Politics':['Politics'], 'Religion':['Religion'], 'Romance':['Romance'],
    'Science':['Science'], 'Science Fiction':['Science Fiction', 'Science-Fiction'],
    'Sexuality':['Sexuality'], 'Short Fiction':['Short Fiction', 'Short-Fiction', 'Short Story '], 'Thriller':['Thriller'],
    'Travel':['Travel'], 'War':['War'], 'Western':['Western'], 
    'Young Readers':['Young Readers', 'Young-Readers', "Children's Literature"]}

# all genres can be used with writer

def search_for_author_information(author):     
    ''' search information about author in wiki '''

    auth_url = "http://en.wikipedia.org/wiki/" + author.name.replace(" ", "_")
    opener = urllib2.build_opener()
    opener.addheaders = [('User-agent', 'Mozilla/5.0')]

    page=""    
    try:
        infile = opener.open(auth_url)
        page = infile.read()
    except:
        is_article = -1

    beaut_soup = bs.BeautifulSoup(page)
    text = beaut_soup.getText()

    is_article = text.find("Wikipedia does not have an article with this exact name")
    text = text[0:0]
    if is_article != -1:
        auth_url = "http://en.wikipedia.org/wiki/Special:Search/" + author.name
        infile = opener.open(auth_url)
        page = infile.read()
        beaut_soup = bs.BeautifulSoup(page)
        did_you = beaut_soup.find(attrs={'class':'searchdidyoumean'})
        if did_you != None:
            did_you = did_you.get('a')
        
        if did_you != None:
            did_you = did_you['href']
            auth_url = "http://en.wikipedia.org" + did_you
            infile = opener.open(auth_url)
            page = infile.read()
            beaut_soup = bs.BeautifulSoup(page)
            text = beaut_soup.getText()
            is_result = text.find('Search results')
            if is_result != -1:
                first_res = beaut_soup.find(attrs={'class':'mw-search-results'})
                article = first_res.find('li')
                if article != None:
                    article = article.find('a')
                    if article != None:
                        auth_url = article['href']
                        infile = opener.open(auth_url)
                        page = infile.read()
                        beaut_soup = bs.BeautifulSoup(page)
                        text = beaut_soup.getText().lower()
                        is_writer = text.find('writer')
                        if is_writer == -1:
                            return
                        else:
                            auth_url = article.find('a')['href']
                            infile = opener.open(auth_url)
                            page = infile.read()
                            beaut_soup = bs.BeautifulSoup(page)
        else:
            first_res = beaut_soup.find(attrs={'class':'mw-search-results'})
            if first_res != None:
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

    found = False
    for i in genres.items():
        t = Tag.objects.get_or_create(name = i[0])
        for j in i[1]:
            k = beaut_soup.find(attrs = {'title':j})
            if k:
                found = True
                author.tag.add(t[0])
            k = beaut_soup.find(attrs = {'title':j.lower()})
            if k:
                found = True
                author.tag.add(t[0])


#    genres_tag = beaut_soup.find(attrs = {'title':'Literary genre'})
#    if not genres_tag:
#        employment_tag = beaut_soup.find(attrs = {'title':'Employment'})
#        if not employment_tag:
#            text = beaut_soup.getText()
#        else:
#            children = employment_tag.findParent().findParent().findChildren()
#            for i in children:
#                text += i.getText() + " "
#    else:
#        children = genres_tag.findParent().findParent().findChildren()
#        for i in children:
#            text += i.getText() + " "

    if found == False:
        text = beaut_soup.getText()
        text = text.lower()
        
        for i in genres.items():
            t = Tag.objects.get_or_create(name = i[0])
            for j in i[1]:
                k = text.count(j.lower())
                if k >= 3:
                    print author.name
                    print t[0].name
                    author.tag.add(t[0])


