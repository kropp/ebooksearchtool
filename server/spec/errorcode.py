'''This file discribes error and warning codes, wich you can get getting/adding/updating information about book, author, book-link etc.

Each code consists 6 digits


The first digit says about type of error

1xxxxx - warning
2xxxxx - error in input data
3xxxxx - error in database
4xxxxx - error on server


Next two digits say about action

x00xxx - test error
x01xxx - get
x02xxx - insert
x03xxx - update
x04xxx - remove


The last three digits say specific information about error

xxx000 - test error

'''

ERROR_CODE = {
	100000: 'test warning',

	200000: 'test error in input data',

	300000: 'test error in database',

	400000: 'test error on server',
}
