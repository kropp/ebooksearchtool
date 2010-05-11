'''script for starting book classifier '''

from django.core.management import setup_environ
import settings
setup_environ(settings)

from book.models import Book, Tag, Annotation
from classifier.search_tools import GENRES
from classifier.read_tools import get_description, read
from classifier.tools import deserialize
import datetime

def add_tag_for_new_books():
    ''' adds tag for books after classifying '''
    classifier = deserialize()
    today = datetime.date.today()
    today.replace(day=today.day-1)

    books = Book.objects.filter(book_file__time_found__gt=today)
    counter = 0
    counter_all = 0
    for book in books:
        counter_all += 1
        if book.tag.count() != 0:
            continue

        title = book.title
        for i in GENRES.items():
            for j in i[1]:
                if title.find(j) != -1:
                    tag = Tag.objects.get_or_create(name=i[0])
                    book.tag.add(tag[0])

        ann = book.annotation_set.all()
        summary = ""
        authors = book.author.all()
        if ann.count() == 0:
            if authors.count() != 0:
                summary = get_description(book.title + authors[0].name, True)
            else:
                summary = get_description(book.title, True)
            if summary == None:
                continue
            ann = Annotation.objects.get_or_create(name=summary, book=book)        

        else:
            for i in ann:
                summary += i.name
                     
            len_sum = len(summary.split())
            if len_sum < 50:
                if authors.count() != 0:
                    summary = get_description(book.title + authors[0].name, 
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
            if counter % 10 == 0:
                print counter, "of cheched ", counter_all
            if tags[0] != None:
                tag = Tag.objects.get_or_create(name=tags[0])
                book.tag.add(tag[0])
            if tags[1] != None:
                tag = Tag.objects.get_or_create(name=tags[1])
                book.tag.add(tag[0])
    print "Classification complete"
    print counter, " books has been classified"

add_tag_for_new_books()
