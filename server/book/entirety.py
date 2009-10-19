'''Object-entiretys: AuthorEntirety, FileEntirety, BookEntirety

All objects support method save_to_db() for save it obkects to database.
Warning: the aren't using transaction. Use @transaction for save database in correct state.

Method get_from_db() returns list of object-entiretys, wich matchin on condition in instance object.'''


import server.book.models as book
from server.book.fileTypes import FILE_TYPE
from server.exception import *



class Callable:
    'Makes method callable without instanse (like static method)'
    def __init__(self, anycallable):
        self.__call__ = anycallable


class AuthorEntirety:
    def __init__(self, name='', aliases=[]):
        self.name = name
        self.aliases = aliases
    
    def save_to_db(self):
        "Saves entirety author to database. Ruturns saved author"
        if not self.name:
            raise InputDataServerEx("The field 'author.name' can't be empty")

        author = book.Author.objects.get_or_create(name=self.name)[0]

        # get all aliases
        for alias_item in self.aliases:
            # get or create alias in database
            alias_obj = book.Alias.objects.get_or_create(name=alias_item)[0]
            alias_id = None
            if alias_obj.author_set.count() > 0:
                alias_id = alias_obj.author_set.get().id
            # if existing alias associated with other author -> exception
            if alias_id and alias_id != author.id:
                raise DataBaseServerEx("Alias already exists ans is associated with other author")
            # add alias to author
            alias_obj.author_set.add(author)
            alias_obj.save()

        return author
       
    def get_from_db(self):
        "Returns list of authors"
        if self.aliases:
            "Get authors by name and alias"
            author_list = []
            for alias_name in aliases:
                author_list += book.Author.objects.filter(name__icontains=self.name, alias__name__icontains=alias__name__icontains)
            # remove duplicates
            author_dict = {}
            for author in author_list:
                author_dict[author.name] = author
            author_list = author_dict
        else:
            "Get authors only by name"
            author_list = book.Author.objects.filter(name__icontains=self.name)
        return author_list
    
    def CreateFromObj(obj):
        print 'I am creating from obj'
    CreateFromObj = Callable(CreateFromObj)



class FileEntirety:
    def __init__(self, link, size=None, type='',
                 more_info='', img_link=''):
        self.link = link
        self.size = size
        self.type = type
        self.more_info = more_info
        self.img_link = img_link

    def save_to_db(self):
        book_file = book.BookFile.objects.get_or_create(link=self.link, size=self.size)[0]
        book_file.size = self.size
        if self.type:
            book_file.type = self.type
        if self.more_info:
            book_file.more_info = self.more_info
        if self.img_link:
            book_file.img_link = self.img_link

        book_file.save()

        return book_file

    def get_from_db(self):
        book_list = book.BookFile.objects.filter(link__icontains=self.link,
                                                 type__icontains=self.type,
                                                 more_info__icontains=self.more_info)
        return book_list




class BookEntirety:
    def __init__(self, title, lang='', authors=[], files=[], annotations=[]):
        self.title = title
        self.lang = lang
        self.authors = authors
        self.files = files
        self.annotations = annotations

    def save_to_db(self):
        book_obj = book.Book.objects.get_or_create(title=self.title)[0]

        for author in self.authors:
            author_obj = author.save_to_db()
            author_obj.book.add(book_obj)

        for file in self.files:
            file_obj = file.save_to_db()
            book_obj.book_file.add(file_obj)
        
        return book_obj.id

    def get_from_db(self):
        "returns list of matched BookEntirety"
        book_list = book.Book.objects.filter(title__icontains=self.title)
        return book_list


