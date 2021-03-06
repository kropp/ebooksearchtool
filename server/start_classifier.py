'''script for starting book classifier '''

from django.core.management import setup_environ
import settings
setup_environ(settings)

from book.models import Book, Tag, Annotation
from classifier.search_tools import GENRES
from classifier.read_tools import get_description, read
from classifier.tools import deserialize
from add_tools.tools import smart_split

def tag_adding(books):
    ''' adds tag for books after classifying '''
    digits = "0123456789"
    classifier = deserialize()

    counter = 0
    counter_all = 0
    for book in books:
        counter_all += 1

        title = book.title
        if book.credit == 0:
            ind = title.find(" trans by ")
            if ind != -1:
                title = title[:ind]
            ind = title.find(" translated ")    
            if ind != -1:
                title = title[:ind]
            ind = title.find(" adapted ")    
            if ind != -1:
                title = title[:ind]
            ind = title.find(" by ")
            if ind != -1:
                title = title[:ind]

            if book.title != title:
                book.title = title
                book.credit = 1
                book.save()

        for i in GENRES.items():
            for j in i[1]:
                if title.find(j) != -1:
                    tag = Tag.objects.get_or_create(name=i[0])
                    book.tag.add(tag[0])
                    continue

        ann = book.annotation_set.all()
        summary = ""
        authors = book.author.all()


        if ann.count() == 0:
            if authors.count() != 0:
                summary = get_description(book.title + "+" + authors[0].name, True)
            else:
                summary = get_description(book.title, True)
            if summary == None:
                continue
            if len(summary) >= 50:
                ann = Annotation.objects.get_or_create(name=summary, book=book)        
            else:
                continue

        else:
            for i in ann:
                summary += i.name
                     
            len_sum = len(summary.split())
            if len_sum < 50:
                if authors.count() != 0:
                    summary = get_description(book.title + "+" + authors[0].name, 
                                                                        True)
                else:
                    summary = get_description(book.title, True)
            if summary == None:
                continue
            ann = Annotation.objects.get_or_create(name=summary, book=book)

        if summary:
            counter += 1        
            tag_ind = summary.lower().find("subjects:")
            if tag_ind != -1:
                summary = summary[:tag_ind].strip()
                tags = smart_split([summary[tag_ind+9:]], [',', '/', '_'])
            else:
                if book.author.all():
                    if book.author.all()[0].tag.count():
                        tags = classifier.classify(summary, 
                                    [x.name for x in book.author.all()[0].tag.all()])
                        for tag in tags:
                            classifier.train(summary, tag)
                    else:
                        tags = classifier.classify(summary)
                        for tag in tags:
                            classifier.train(summary, tag)
                else:
                    tags = classifier.classify(summary)
                    for tag in tags:
                        classifier.train(summary, tag)
            if not counter%10:
                print counter, "of cheched ", counter_all

            if tags[0:1]:
                tag = Tag.objects.get_or_create(name=tags[0])
                book.tag.add(tag[0])
            if tags[1:2]:
                tag = Tag.objects.get_or_create(name=tags[1])
                book.tag.add(tag[0]) 

    print "Classification complete"
    print counter, " books has been classified"
   
books = Book.objects.exclude(tag__isnull=False)
tag_adding(books)    
