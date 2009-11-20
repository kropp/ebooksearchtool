"Insert action handler"

import md5

from server.book.models import Book, Author, Alias, BookFile

def strip_str(str):
    '''Removes leading, endig space from string,
    Returns empty string, if str is None'''
    if str:
        return str.strip()
    return ''

def set_if_notempty(value, key):
    "Sets to value key, if key isn't empty"
    if key:
        value = key


def get_authors(node):
    '''Creates or finds authors, returns authors list'''
    authors = []

    for author_node in node.getchildren():
        author = None

        # TODO add by id 

        # look in tag author
        for details_node in author_node.getchildren():
            # create or find author with name from tag
            if details_node.tag == 'name':
                author_name = strip_str(details_node.text)
                if author_name:
                    author = Author.objects.get_or_create(name=author_name)[0]

            if details_node.tag == 'alias' and author:
                alias_name = strip_str(details_node.text)
                if alias_name:
                    alias = Alias.objects.get_or_create(name=alias_name)[0]
                    author.alias.add(alias)
        # add author to list, if it is created or found
        if author:
            authors.append(author)
    return authors


def get_files(node):
    '''Creates or finds files, returns files list'''
    book_files = []

    for file_node in node.getchildren():
        book_file = None

        # look in tag file
        for details_node in file_node.getchildren():
            # create or find file with link from tag
            if details_node.tag == 'link':
                link = strip_str(details_node.text)
                if link:
                    link_hash = md5.new(link).hexdigest()
                    book_file = BookFile.objects.get_or_create(link=link,
                                                        link_hash=link_hash)[0]

            # add size filed if exists book_file and size is int
            if details_node.tag == 'size' and book_file:
                size = strip_str(details_node.text)
                try:
                    size = int(size)
                    book_file.size = size
                except ValueError:
                    pass
                
            if details_node.tag == 'type' and book_file:
                type = strip_str(details_node.text)
                # TODO check type
                if type:
                    book_file.type = type

            if details_node.tag == 'more_info' and book_file:
                set_if_notempty(book_file.more_info,
                                strip_str(details_node.text))

            if details_node.tag == 'img_link' and book_file:
                set_if_notempty(book_file.img_link,
                                strip_str(details_node.text))

        # add book_file to list, if it is created or found
        if book_file:
            book_file.save()
            book_files.append(book_file)
    return book_files


def xml_exec_insert(xml):
    "Insert xml request to dsta base, returns list of warning strings"
    
    #if xml.tag != 'book':
    #    raise InputDataServerException("Not found root tag 'book'")

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

    # TODO make warnings
    return []
        
