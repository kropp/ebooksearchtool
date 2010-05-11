'''script for starting book classifier '''

from django.core.management import setup_environ
import settings
setup_environ(settings)

from book.models import Book, Tag, Annotation
from classifier.search_tools import GENRES
from classifier.read_tools import get_description, read
from classifier.tools import deserialize
from add_tools.tools import smart_split

def tag_adding():
    ''' adds tag for books after classifying '''
    classifier = deserialize()
    books = Book.objects.exclude(tag__isnull=False)
    counter = 0
    counter_all = 0
    for book in books:
        counter_all += 1

        title = book.title
        if book.credit == 0:
            title = title[:title.find(" trans by ")]
            title = title[:title.find(" translated ")]
            title = title[:title.find(" adapted ")]
            title = title[:title.find(" by ")]

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

        if author.credit == 0:
            for author in authors:
                author_string = author.name
                author_string = author_string[author_string.find(" trans by ")+10:]
                author_string = author_string[author_string.find(" translated ")+12:]
                author_string = author_string[author_string.find(" adapted ")+9:]
                author_string = author_string[author_string.find(" by ")+4:]
                if author_string and author.name != author_string:
                    author.name = author_string
                    author.credit = 1
                    author.save()

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
                tags = classifier.classify(summary)
            if not counter%10:
                print counter, "of cheched ", counter_all
            if tags[0] != None:
                tag = Tag.objects.get_or_create(name=tags[0])
                book.tag.add(tag[0])
            if tags[1] != None:
                tag = Tag.objects.get_or_create(name=tags[1])
                book.tag.add(tag[0]) 

    print "Classification complete"
    print counter, " books has been classified"
   

tag_adding()    
