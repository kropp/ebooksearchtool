#ifndef _NETWORK_MANAGER_H_
#define _NETWORK_MANAGER_H_

class HttpConnection;
// singleton

class NetworkManager {

private:
    static NetworkManager* instance;

public:
    static NetworkManager* getInstance();

private:
    NetworkManager();
    
private:
    HttpConnection* myHttpConnection;   
};

#endif //_NETWORK_MANAGER_H_
