#include <QtNetwork>

#include "httpconnection.h"
#include "../configurator/configurator.h"

#include <iostream>

QString HttpConnection::ourConfigFilePath = "../.config/network";
QString HttpConnection::ourProxy = "undefined";
int HttpConnection::ourPort = 80;
QString HttpConnection::ourServer = "undefined";


HttpConnection::HttpConnection(QObject* parent = 0) : QHttp(parent) {
    configurate();
}

void HttpConnection::downloadFile(QString urlStr, QFile* file) {
    if (ourProxy != "undefined") { 
        setProxy(ourProxy, ourPort);
	}
	setHost(ourServer, 80); // не работает с ourPort
	QString query(urlStr);
	query.remove("http://");
	query.remove(ourServer); //оставляю только запрос
	myHttpGetId = get(query, file);
}
    
void HttpConnection::configurate() {
    QSettings settings("../.config.ini", QSettings::IniFormat);
    ourProxy = settings.value("network/proxy").toString();
    ourPort = settings.value("network/port").toInt();
    ourServer = settings.value("network/server").toString();
    //std::cout << "proxy " << ourProxy.toStdString().c_str() << "\n";
    //std::cout << "port " << ourPort << "\n";
    //std::cout << "server" << ourServer.toStdString().c_str() << "\n";
}

QString HttpConnection::getServer() const {
    return ourServer;
}

