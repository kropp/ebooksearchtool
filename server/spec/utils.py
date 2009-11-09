from string import join, split

SERVER_URL = "http://only.mawhrin.net/ebooks"

def convert_delim(strs, delimiter=' '):
    '''Convert all \\r \\n \\t to delimiter.
       Input parametr: strs is one string or list of strings'''
    if isinstance(strs, list):
        result = map(lambda x: join(split(x), delimiter), strs)
    else:
        result = join(split(strs), delimiter)
    return result


def replace_delim_to_space(str, delimiter=' '):
    if str:
        return join(split(str), delimiter)
    return ''

