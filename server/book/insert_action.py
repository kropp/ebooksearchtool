"Insert action handler"

from server.book.models import Book, Author, Alias

def strip_str(str):
    '''Removes leading, endig space from string,
    Returns empty string, if str is None'''
    if str:
        return str.strip()
    return ''


def get_authors(node):
    '''Creates or finds authors, returns authors list'''
    authors = []

    for author_node in node.getchildren():
        author = None

        # look in tag author
        for details_node in author_node.getchildren():
            # create or find author with name from tag
            if details_node.tag == 'name':
                author_name = strip_str(details_node.text)
                if author_name:
                    author = Author.objects.get_or_create(name=author_name)[0]

            if details_node.tag == 'alias':
                alias_name = strip_str(details_node.text)
                if alias_name and author:
                    alias = Alias.objects.get_or_create(name=alias_name)[0]
                    author.alias.add(alias)
        # add author to list, if it is created or found
        if author:
            authors.append(author)
    return authors


def xml_exec_insert(xml):
    "Insert xml request to dsta base"
    
    if xml.tag != 'book':
        raise InputDataServerException("Not found root tag 'book'")

    book = Book(title='', lang='')
    authors = []
    files = []
    annotations = []

    for node in xml.getchildren():
        if node.tag == 'title':
            book.title = strip_str(node.text)

        if node.tag == 'lang':
            # TODO add check of correct lang (from LANG_CODE)
            book.lang = strip_str(node.text)
        
