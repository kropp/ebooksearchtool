from book.models import Author

from spec.search_util import rm_items, id_field

def author_search(query, max_length=5):
    """Searches 'query' in author field.
    Returns list of SphinxSearch."""

    result_list = []

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
