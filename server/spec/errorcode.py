'''This file discribes error and warning codes, wich you can get getting/adding/updating information about book, author, book-link etc.

Each code consists 5 digits


The first digit says about type of error

1xxxx - warning
2xxxx - error in input data
3xxxx - error in database
4xxxx - error on server

5xxxx - error in request

Next digit says about action

x0xxx - unknow error

x1xxx - get
x2xxx - insert
x3xxx - update
x4xxx - remove


The last three digits say specific information about error

xx000 - unknown error

'''

ERROR_CODE = {
	10000: 'test warning',

	20000: 'Unknow error in input data',
	21101: 'The field author\'s name can\'t be empty',
	22101: 'The field author\'s name can\'t be empty',

	30000: 'Unknow error in database',
	32101: 'Author with this name exists in database',

	40000: 'test error on server',

    50001: 'Bad request method. Use POST'
}
