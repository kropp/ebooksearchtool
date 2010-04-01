
from settings import MAX_RESULT_LENGTH

from book.models import Author, Book

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


search_engine = SphinxSearchEngine()

search_engine.update_query(author='tolstoy anna')
authors = search_engine.get_result('author')
#print authors

# search_engine.clear_query()
search_engine.update_query(title='tolstoy anna')
books = search_engine.get_result('book')

print books



