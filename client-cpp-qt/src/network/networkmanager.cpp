#include <QSettings>
#include <QString>
#include <QHttp>
#include <QProgressBar>
#include <QUrl>
#include <QDebug>

#include "networkmanager.h"

NetworkManager* NetworkManager::instance = 0;

QString NetworkManager::ourConfigFilePath = "../.config.ini";
QString NetworkManager::ourProxy = "undefined";
int NetworkManager::ourPort = 80;    
QString NetworkManager::ourServer = "undefined";

NetworkManager* NetworkManager::getInstance() {
    if (instance == 0) {
        instance = new NetworkManager();
    }
    return instance;
}

void NetworkManager::setServer(const QString& newServer) {
    ourServer = newServer;
}

NetworkManager::NetworkManager() {
    myHttpConnection = new QHttp(this);
    myConnectionForCovers = new QHttp(this);
    readSettings();
    connect(myHttpConnection, SIGNAL(requestFinished(int, bool)), this, SIGNAL(requestFinished(int, bool)));
    connect(myConnectionForCovers, SIGNAL(requestFinished(int, bool)), this, SIGNAL(coverRequestFinished(int, bool)));
    connect(myHttpConnection, SIGNAL(dataReadProgress(int, int)), this, SIGNAL(dataReadProgress(int, int)));
    connect(myHttpConnection, SIGNAL(stateChanged(int)), this, SLOT(showConnectionState(int)));
}

NetworkManager::~NetworkManager() {
    writeSettings();
}

QString NetworkManager::errorString() const {
    return myHttpConnection->errorString();
}

void NetworkManager::readSettings() {
    QSettings settings(ourConfigFilePath, QSettings::IniFormat);
    ourProxy = settings.value("network/proxy").toString();
    ourPort = settings.value("network/port").toInt();
    ourServer = settings.value("network/server").toString();
}

void NetworkManager::writeSettings() const {
    QSettings settings(ourConfigFilePath, QSettings::IniFormat);
    settings.beginGroup("network");
    settings.setValue("proxy", ourProxy);
    settings.setValue("port", ourPort);
    settings.setValue("server", ourServer);
    settings.endGroup();
} 

int NetworkManager::download(QString urlStr, QIODevice* out) {
	myHttpConnection->setHost(ourServer, 80);
    if (ourProxy != "undefined") { 
        myHttpConnection->setProxy(ourProxy, ourPort);
	}
	QString query(urlStr);
	query.remove("www.");
	query.remove("http://");
	query.remove(ourServer); //оставляю только запрос

    qDebug() << "NetworkManager::download request =" << ourServer <<  query;
	int id = myHttpConnection->get(query, out);
    return id;
}

int NetworkManager::downloadCover(QString urlStr, QIODevice* out) {
    //TODO extract Host from url
    QUrl url(urlStr);
    myConnectionForCovers->setHost(url.host(), 80);
    if (ourProxy != "undefined") { 
        myConnectionForCovers->setProxy(ourProxy, ourPort);
	}

    qDebug() << "NetworkManager::downloadCover request =" << url.host()<<  url.path();
	int id = myConnectionForCovers->get(url.path(), out);
    return id;
}

void NetworkManager::showConnectionState (int state) {
    //qDebug() << "NetworkManager::connectionState " << state;
    // 0 unconnected
    // 1 host lookup
    // 2 connecting
    // 3 sending
    // 4 reading
    // 5 connected
    // 6 closing
}
