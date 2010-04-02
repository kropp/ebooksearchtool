
from settings import MAX_RESULT_LENGTH

from book.models import Author, Book, Language


class KateSearchEngine():

    def simple_search(self, **kwargs):
        pass

    def search_author(self, **kwargs):
        pass

    def search_book(self, **kwargs):
        pass


class SphinxKateSearchEngine(KateSearchEngine):

    def simple_search(self, query, **kwargs):
        tmp = dict(kwargs)
        tmp['title'] = query
        tmp['author'] = query
        books = list(self.search_book(**tmp))
        books_set = set(books)

        del tmp['author']
        books_a = self.search_book(**tmp)

        for book in books_a:
            if not book in books_set:
                books.append(book)

        return books
    
    def search_author(self, **kwargs):
        author_query = kwargs.get('author')
#        lang_query = kwargs.get('lang')
        tag_query = kwargs.get('tag')

        if author_query:
            authors = Author.soundex_search.query(author_query)
            
#            if lang_query:
#                lang_id = Language.objects.get(short=lang_query).id
#                authors = authors.filter(language_id=lang_id)

            if tag_query:
                tag_id = Tag.objects.get(name=tag_query).id
                authors = authors.filter(tag_id=tag_id)

            return authors


    def search_book(self, **kwargs):
        title_query = kwargs.get('title')
        author_query = kwargs.get('author')
        lang_query = kwargs.get('lang')
        tag_query = kwargs.get('tag')
        

        if title_query:
            books = Book.title_search.query(title_query)
            
            if lang_query:
                lang_id = Language.objects.get(short=lang_query).id
                books = books.filter(language_id=lang_id)

            if tag_query:
                tag_id = Tag.objects.get(name=tag_query).id
                books = books.filter(tag_id=tag_id)

            if author_query:
                authors = Author.soundex_search.query(author_query)
                authors_id = map(lambda x: x.id, authors)
                print authors_id
                books = books.filter(author_id=authors_id)

            return books



class SearchEngine(object):
    "Abstract class, interface for search engine"

    def __init__(self):
        self.__query__ = {}
        self.max_result_length = MAX_RESULT_LENGTH

    def update_query(self, **kwargs):
        "Adds args to query dictionary."
        self.__query__.update(kwargs)

    def clear_query(self):
        "Clears query dectionary."
        self.__query__.clear()

    def get_result(self, entity):
        "Searchs query. Returns list of entity."
        raise NotImplementedError(
            'SearchEngine.get_result() must be implemented in subclasses')


class SphinxSearchEngine(SearchEngine):
    "Search engine using sphinx for searching."

    def __init__(self):
        super(SphinxSearchEngine, self).__init__()
        self.__results__ = {}

    def __author_search__(self):
        self.__results__['author'] = []
        query = self.__query__.get('author')
        if query:
            authors = Author.soundex_search.query(query)
            self.__results__['author'] = authors

    def __book_search__(self):
        self.__results__['book'] = []
        query = self.__query__.get('title')
        if query:

            books = Book.title_search.query(query)

            self.__author_search__()
            if self.__results__['author']:
                authors_id = map(lambda x: x.id, self.__results__['author'])

                books = books.filter(author_id=authors_id)

            self.__results__['book'] = books
    
    def get_result(self, entity):
        if entity == 'author':
            self.__author_search__()
            return self.__results__['author'][0:self.max_result_length]
        elif entity == 'book':
            self.__book_search__()
            return self.__results__['book'][0:self.max_result_length]


SphinxKateSearchEngine()
search_engine = SphinxKateSearchEngine()

print list(search_engine.search_author(author='tolstoy '))
print
print list(search_engine.search_book(title='anna karenina', author='tolstoy'))
print
print list(search_engine.simple_search('tolsloy anna karenina', lang='en'))





