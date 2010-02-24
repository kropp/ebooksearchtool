#ifndef _NETWORK_MANAGER_H_
#define _NETWORK_MANAGER_H_

#include <QObject>
#include <QMap>

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
    static QString ourCurrentServer;

    static QMap<QString, QString> ourServersSearchSchema; // map server->opensearch schema

private:
    static NetworkManager* instance;

public:
    static NetworkManager* getInstance();
    static void setServer(const QString& newServer);
    static const QString& getServer();
    static void initializeMap();

    const QString& getProxy() const;
    int getPort() const;    
    void getServers(QList<QString>& servers) const;
public:
    ~NetworkManager();
	
	int download(QString url, QIODevice* out);
	int downloadCover(QString url, QIODevice* out);
    QString errorString() const;

signals:
    void requestFinished(int, bool);
    void coverRequestFinished(int, bool);
    void dataReadProgress(int, int);

private:
    NetworkManager();
    void readSettings();
    void writeSettings() const;   

private slots:
    void showConnectionState (int state);

private:
    QHttp* myHttpConnection;   
    QHttp* myConnectionForCovers;   

friend class InternetConnectionDialog;
};

inline const QString& NetworkManager::getProxy() const {
    return ourProxy;
}

inline int NetworkManager::getPort() const {
    return ourPort;
}    

inline const QString& NetworkManager::getServer() {
    return ourCurrentServer;
}

#endif //_NETWORK_MANAGER_H_
