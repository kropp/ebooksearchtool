#ifndef _CONNECTION_PARAMETERS_H_
#define _CONNECTION_PARAMETERS_H_

class ConnectionParameters {

public:
    ConnectionParameters(const QString& proxy, int port) : myProxy(proxy),
                                                           myPort(port) {}

public:
    const QString& getProxy() const;
    int getPort() const;

private:
    const QString& myProxy;
    const int myPort;
};

inline const ConnectionParameters::QString& getProxy() const {
    return myProxy;
}

inline int ConnectionParameters::getPort() const {
    return myPort;
}

#endif //_CONNECTION_PARAMETERS_H_
    
