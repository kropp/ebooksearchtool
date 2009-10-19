#include <QtNetwork>

#include "httpconnection.h"
#include "../configurator/configurator.h"

#include <iostream>

QString HttpConnection::ourConfigFilePath = "../.config/network";
QString HttpConnection::ourProxy = "undefined";
int HttpConnection::ourPort = 80;
QString HttpConnection::ourServer = "undefined"; // далее изначально неопределен


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
//typedef std::map<const std::string, std::string*> Map;
    Map settings;
    std::string name1("PROXY");
    std::string name2("PORT");
    std::string name3("SERVER");
    std::string value1;
    std::string value2;
    std::string value3;
    settings.insert(std::make_pair(name1, &value1));
    settings.insert(std::make_pair(name2, &value2));
    settings.insert(std::make_pair(name3, &value3));

    Configurator configurator;
    configurator.setParameters(ourConfigFilePath.toStdString(), settings);
    ourProxy = value1.c_str();
    QString str(value2.c_str());
    ourPort = str.toInt();
    ourServer = value3.c_str();
//    std::cout << "server = " << ourServer.toStdString().c_str() << "\n";
//    std::cout << "port = " << ourPort << "\n";
}

QString HttpConnection::getServer() const {
    return ourServer;
}

