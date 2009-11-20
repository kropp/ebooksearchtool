#include <QSettings>
#include <QString>
#include <QHttp>
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

NetworkManager::NetworkManager() {
    myHttpConnection = new QHttp(this);
    configurate();
    connect(myHttpConnection, SIGNAL(requestFinished(int, bool)), this, SIGNAL(requestFinished(int, bool)));
}

//NetworkManager::~NetworkManager() {}

void NetworkManager::configurate() {
    QSettings settings(ourConfigFilePath, QSettings::IniFormat);
    ourProxy = settings.value("network/proxy").toString();
    ourPort = settings.value("network/port").toInt();
    ourServer = settings.value("network/server").toString();
    qDebug() << "server from config file " << ourServer;
}

QString NetworkManager::getServer() const {
    return ourServer;
}

void NetworkManager::download(QString urlStr, QIODevice* out) {
    if (ourProxy != "undefined") { 
        myHttpConnection->setProxy(ourProxy, ourPort);
	}
	myHttpConnection->setHost(ourServer, 80); // не работает с ourPort
	QString query(urlStr);
	query.remove("http://");
	query.remove(ourServer); //оставляю только запрос
	myHttpConnection->get(query, out);
}

