''' Exception hierarchy

ServerEx

    RequestServerEx - bad request

    RequestFileServerEx - bad request file format

    InputDataServerEx - bad input data

    DataBaseServerEx - request is not accommodated with database


'''

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




class ServerEx(Exception):
    "Base server exeption"
    pass

class RequestServerEx(ServerEx):
    "Bad request"
    pass

class RequestFileServerEx(ServerEx):
    "Bad request file format"
    pass

class InputDataExcpt(ServerEx):
    "Bad input data"
    pass

class DatabaseExcp(ServerEx):
    "Request is not accommodated with database"
    pass

