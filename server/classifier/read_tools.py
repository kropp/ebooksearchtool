''' Package for reading feeds from different sources '''

import spec.external.feedparser as feedparser
import urllib2

from spec.external.BeautifulSoup import BeautifulSoup as bs

from book.models import Language

def get_description(book_name, flag = False):
    ''' gets book description from Amazon.com'''
    
    name = book_name.replace(" ", "+")
#    print name
    if flag == True:
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

def read(b, feed, classif):
    ''' gets URL and classify items '''
    # get feed items
    f = feedparser.parse(feed)

    counter = 0
    if b == True:
        file_handle = open("statistics", "a")
    
    for entry in f['entries']:
        summary = entry['summary'].encode('utf-8')
        
        len_sum = len(summary.split())
        if len_sum < 10:
            summary = get_description(entry['title'].encode('utf-8'))
            if summary == None:
                break

        # find author and his tags
#        author_ref = entry['author_detail']['href'].encode('utf-8')
#        auth_parser = feedparser.parse(author_ref)

        counter = counter+1
       
#        print entry['title'].encode('utf-8')
#        fulltext = '%s\n%s' % (entry['title'].encode('utf-8'), summary)

        fulltext = summary
        
        short_lang = entry.get('dcterms_language')
        
        # different languages stemming
        language = Language.objects.filter(short=short_lang)
        
        if language.count() == 0:
            hyp = classif.classify(fulltext)
        else:
            hyp = classif.classify(fulltext, lang=language[0].full.lower())
        
#        if short_lang.lower() == "ru":
#            hyp = classif.classify(fulltext, lang="russian")
#        else:
#            hyp = classif.classify(fulltext)
            
#        print 'Hypothesis: ' + str(hyp)
        
        if entry.get('categories') == None:
            break
                        
        for cat in entry.get('categories'):
            c = cat[1].encode('utf-8')
#            print ' Tag : ' + c
            if b == False:
                if language.count() == 0:
                    hyp = classif.train(fulltext, c)
                else:
                    hyp = classif.train(fulltext, c, lang=language[0].full.lower())
            
            # write statistic

            if b == True and (c == hyp[0] or c == hyp[1]):     
                file_handle.write(c)
                file_handle.write('\n')    

    if b == True:
        file_handle.close()
                
    return counter

def read_smashwords(b, feed, classif):
    ''' gets URL and classify items from smashwords '''
    # get feed items
    f = feedparser.parse(feed)

    counter = 0
    if b == True:
        file_handle = open("statistics", "a")
    
    for entry in f['entries']:
        subtitle = entry['subtitle'].encode('utf-8')
        beaut_soup = bs.BeautifulSoup(subtitle)
        text = beaut_soup.getText()
        summary = text[0:text.find(u'Price')]

        len_sum = len(summary.split())
        if len_sum < 10:
            summary = get_description(entry['title'].encode('utf-8'))
            if summary.__str__() == "None":
                break

        # find author and his tags
#        author_ref = entry['author_detail']['href'].encode('utf-8')
#        auth_parser = feedparser.parse(author_ref)

        counter = counter+1
       
#        print entry['title'].encode('utf-8')
#        fulltext = '%s\n%s' % (entry['title'].encode('utf-8'), summary)

        fulltext = summary
        
        # find language
        index = text.find(u'Language')
        short_lang = text[index + 9:index+11]
        
        # different languages stemming
        language = Language.objects.filter(short=short_lang)
        
        if language.count() == 0:
            hyp = classif.classify(fulltext)
        else:
            hyp = classif.classify(fulltext, lang=language[0].full.lower())
            
#        print 'Hypothesis: ' + str(hyp)
                    
        subj_index = text.find('Subject') + 8
        subjects = text[subj_index:index].split(',')
        
        for c in subjects:
#            print ' Tag : ' + c
            if b == False:
                if language.count() == 0:
                    hyp = classif.train(fulltext, c)
                else:
                    hyp = classif.train(fulltext, c, lang=language[0].full.lower())
            
            # write statistic

            if b == True and (c == hyp[0] or c == hyp[1]):     
                file_handle.write(c)
                file_handle.write('\n')    

    if b == True:
        file_handle.close()
                
    return counter
    
def read_all_romance(b, feed, classif):
    ''' gets URL and classify items from smashwords '''
    # get feed items
    f = feedparser.parse(feed)

    counter = 0
    if b == True:
        file_handle = open("statistics", "a")
    
    for entry in f['entries']:
        subtitle = entry['subtitle'].encode('utf-8')
        beaut_soup = bs.BeautifulSoup(subtitle)
        text = beaut_soup.getText()
        
        index_about = text.find("About the book")
        summary = text[index_about+15:-1]

        len_sum = len(summary.split())
        if len_sum < 10:
            summary = get_description(entry['title'].encode('utf-8'))
            if summary.__str__() == "None":
                break

        # find author and his tags
#        author_ref = entry['author_detail']['href'].encode('utf-8')
#        auth_parser = feedparser.parse(author_ref)

        counter = counter+1
       
#        print entry['title'].encode('utf-8')
#        fulltext = '%s\n%s' % (entry['title'].encode('utf-8'), summary)

        fulltext = summary
        
        # find language
        index = text.find(u'Language')
        short_lang = text[index + 9:index+11]
        
        # different languages stemming
        language = Language.objects.filter(short=short_lang)
        
        if language.count() == 0:
            hyp = classif.classify(fulltext)
        else:
            hyp = classif.classify(fulltext, lang=language[0].full.lower())
            
#        print 'Hypothesis: ' + str(hyp)
                    
        subj_index = text.find('Subject') + 8
        subjects = text[subj_index:index_about].split(',')
        
        for c in subjects:
#            print ' Tag : ' + c
            if b == False:
                if language.count() == 0:
                    hyp = classif.train(fulltext, c)
                else:
                    hyp = classif.train(fulltext, c, lang=language[0].full.lower())
            
            # write statistic

            if b == True and (c == hyp[0] or c == hyp[1]):     
                file_handle.write(c)
                file_handle.write('\n')    

    if b == True:
        file_handle.close()
                
    return counter
