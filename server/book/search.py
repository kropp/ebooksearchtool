from book.models import Author, Book

from settings import ANALYZER_DEFAULT_RESULT_LENGTH

from spec.exception import RequestServerException
from spec.search_util import rm_items, id_field

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
        soundex_query_set_length = max_length - query_set.count()
        # soundex search
        soundex_query_set = \
            Author.soundex_search.query(query)[0:max_length]

        # TODO should i sort query set using distance between words?

        # remove found in simple search results from soundex search
        id_set = set( [a.id for a in result_list] )
        soundex_query_set = rm_items(soundex_query_set, id_set, id_field)
        result_list.extend(soundex_query_set)

    return result_list




def book_title_search(query, max_length=5):
    """Searches 'query' in book.title field.
    Returns list of SphinxSearch."""

    result_list = []

    # simple full-text search
    query_set = Book.title_search.query(query)
    result_list = query_set[0:max_length]

    return result_list





def xml_search(xml):

    author_node = xml.find('author')
    if author_node is None:
        raise Exception("Not found tag 'author'")

    result_length_node = xml.find('result_length')
    if result_length_node is None:
        result_length = ANALYZER_DEFAULT_RESULT_LENGTH
    else:
        try:
            result_length = int(result_length_node.text)
        except ValueError, ex:
            raise RequestServerException("Value of 'result_length' is not int")

    query_node = author_node.find('query')
    if query_node is None:
        raise Exception("Not found tag 'author.query'")

    return author_search(query_node.text, result_length)

