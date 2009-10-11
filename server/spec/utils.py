from string import join, split

def convert_delim(strs, delimiter=' '):
    '''Convert all \\r \\n \\t to delimiter.
       Input parametr: strs is one string or list of strings'''
    if isinstance(strs, list):
        result = map(lambda x: join(split(x), delimiter), strs)
    else:
        result = join(split(strs), delimiter)
    return result

