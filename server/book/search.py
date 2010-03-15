"Search functions by author, title of bood, etc"

from book.models import Author, Book

from settings import ANALYZER_DEFAULT_RESULT_LENGTH

from spec.exception import RequestServerException
from spec.search_util import rm_items, id_field
from spec.distance import name_distance

def author_search(query, max_length=5):
    """Searches 'query' in author field.
    Returns list of SphinxSearch."""

    # TODO search by author name and alias

    result_list = []

    if not query:
        return []

    # simple full-text search
    query_set = Author.simple_search.query(query)
    result_list = query_set[0:max_length]

    if query_set.count() < max_length:
        # if number of the results is less then max_length
        # than try to search using soundex
        soundex_query_set = \
            Author.soundex_search.query(query)[0:max_length]

        # TODO should i sort query set using distance between words?


        # remove found in simple search results from soundex search
        id_set = set( [a.id for a in result_list] )
        soundex_query_set = rm_items(soundex_query_set, id_set, id_field)
        result_list.extend(soundex_query_set)

    # calculating distance between words
    for item in result_list:
        item.rel = name_distance(query, item.name)

    # sorting list by element relevant index
    result_list.sort( lambda x, y: cmp(x.rel, y.rel) )

    return result_list



def book_title_search(query, author_ids = None, max_length=5):
    """Searches 'query' in book.title field.
    Filter by list of author id.
    Returns list of SphinxSearch."""

    result_list = []

    # simple full-text search
    query_set = Book.title_search.query(query)
    # filter by authors id
    if author_ids:
        # TODO unexpected behavior!
        # filter returns book if intersection of 
        # Book.author_id and [query] is not empty
        # But i want to get books only when Book.author_id contains [query]
        query_set = query_set.filter(author_id=author_ids)
    result_list = query_set[0:max_length]


    # calculating distance between words
    for item in result_list:
        item.rel = name_distance(query, item.title)

    # sorting list by element relevant index
    result_list.sort( lambda x, y: cmp(x.rel, y.rel) )

    return result_list


def xml_search(xml):
    "Executes search request. Returns the the tuple (result_type, result list)"
    results = ('authors', [])

    author_query = ''
    author_ids = []
    book_query = ''

    # get length of results from request
    result_length_node = xml.find('result_length')
    if result_length_node is None:
        result_length = ANALYZER_DEFAULT_RESULT_LENGTH
    else:
        try:
            result_length = int(result_length_node.text)
        except ValueError:
            raise RequestServerException(
                    "Value of 'result_length' is not 'int'")

    # get query to author
    author_node = xml.find('author')
    if author_node is not None:
        # get search term
        query_node = author_node.find('query')
        if query_node is not None:
            author_query = query_node.text

    # get query to book
    book_node = xml.find('book_title')
    if book_node is not None:
        # get list of id's
        ids_node = book_node.find('authors_id')
        if ids_node is not None:
            id_nodes = ids_node.findall('author_id')
            for id_node in id_nodes:
                try:
                    author_ids.append(int(id_node.text))
                except ValueError:
                    raise RequestServerException(
                            "Value of 'book/authors_id/author_id' is not 'int'")
        # get search term
        query_node = book_node.find('query')
        if query_node is not None:
            book_query = query_node.text

    
    # make result
    if author_query:
        results = ('authors', author_search(author_query, result_length))
    elif book_query:
        results = ('books', book_title_search(book_query,
                                              author_ids,
                                              result_length))

    return results
        

