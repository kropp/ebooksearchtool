"Data modify exception"

from server.spec.errorcode import ERROR_CODE

class DataExcpt(Exception):
    "Data modify exception"
    def __init__(self, code, message=''):
        Exception.__init__(self, str(code) + ': ' + message)
        self.code = code
        self.message = message
  
    def get_dict(self):
        "Return dict with information about error"
        # warning
        if self.code < 20000:
            dict = {'status': 'warning',}
        # error
        else:
            dict = {'status': 'error',}
        dict['code'] = self.code
        dict['message'] = ERROR_CODE[self.code]
        return dict

class InputDataExcpt(DataExcpt):
    "Error in input data"
    pass

class DatabaseExcp(DataExcpt):
    "Error in database"
    pass

