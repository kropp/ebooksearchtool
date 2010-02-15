#ifndef _NETWORK_MANAGER_H_
#define _NETWORK_MANAGER_H_

#include <QObject>
//#include <../gui/dialogs/internetConnectionDialog.h>

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
    static void setServer(const QString& newServer);
    const QString& getProxy() const;
    int getPort() const;    
    

public:
    ~NetworkManager();
	
	int download(QString url, QIODevice* out);
	QString getServer() const;

signals:
    void requestFinished(int, bool);
    void dataReadProgress(int, int);

private:
    NetworkManager();
    void readSettings();
    void writeSettings() const;   

private:
    QHttp* myHttpConnection;   

friend class InternetConnectionDialog;
};

inline const QString& NetworkManager::getProxy() const {
    return ourProxy;
}

inline int NetworkManager::getPort() const {
    return ourPort;
}    


#endif //_NETWORK_MANAGER_H_
