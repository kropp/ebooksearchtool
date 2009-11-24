#ifndef _NETWORK_MANAGER_H_
#define _NETWORK_MANAGER_H_

#include <QObject>

class HttpConnection;
class QString;
class QIODevice;
class QHttp;

class NetworkManager : public QObject {

    Q_OBJECT

private:
    static QString ourConfigFilePath;
    static QString ourProxy;
    static int ourPort;    
    static QString ourServer;

private:
    static NetworkManager* instance;

public:
    static NetworkManager* getInstance();
    const QString& getProxy() const;
    int getPort() const;    
    

public:
	void configurate();
	void download(QString url, QIODevice* out);
	QString getServer() const;

signals:
    void requestFinished(int, bool);

private:
    NetworkManager();
        
private:
    QHttp* myHttpConnection;   
};

inline const QString& NetworkManager::getProxy() const {
    return ourProxy;
}

inline int NetworkManager::getPort() const {
    return ourPort;
}    


#endif //_NETWORK_MANAGER_H_
