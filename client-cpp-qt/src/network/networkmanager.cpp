#include <QSettings>
#include <QString>
#include <QHttp>
#include <QProgressBar>
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
    readSettings();
    connect(myHttpConnection, SIGNAL(requestFinished(int, bool)), this, SIGNAL(requestFinished(int, bool)));
    connect(myHttpConnection, SIGNAL(dataReadProgress(int, int)), this, SIGNAL(dataReadProgress(int, int)));
}

NetworkManager::~NetworkManager() {
    writeSettings();
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

    //qDebug() << "NetworkManager::download request =" << ourServer <<  query;
	int id = myHttpConnection->get(query, out);
    return id;
}

