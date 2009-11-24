#include <QSettings>
#include <QString>
#include <QHttp>
//#include <QDebug>

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

void NetworkManager::configurate() {
    QSettings settings(ourConfigFilePath, QSettings::IniFormat);
    ourProxy = settings.value("network/proxy").toString();
    ourPort = settings.value("network/port").toInt();
    ourServer = settings.value("network/server").toString();
}

QString NetworkManager::getServer() const {
    return ourServer;
}

void NetworkManager::download(QString urlStr, QIODevice* out) {
	myHttpConnection->setHost(ourServer, 80);
    if (ourProxy != "undefined") { 
        myHttpConnection->setProxy(ourProxy, ourPort);
	}
	QString query(urlStr);
	query.remove("http://");
	query.remove(ourServer); //оставляю только запрос
	myHttpConnection->get(query, out);
}

