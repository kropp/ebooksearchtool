#include <QtNetwork>

#include "httpconnection.h"
#include "../configurator/configurator.h"

#include <iostream>

QString HttpConnection::ourConfigFilePath = "../.config/network";
QString HttpConnection::ourProxy = "undefined";
int HttpConnection::ourPort = 0;



HttpConnection::HttpConnection(QObject* parent = 0) : QHttp(parent) {
    configurate();
}

void HttpConnection::downloadFile(QString urlStr, QFile* file) {
	QUrl url(urlStr);
	QHttp::ConnectionMode mode = url.scheme().toLower() == "https" ? QHttp::ConnectionModeHttps : QHttp::ConnectionModeHttp;
	setHost(url.host(), mode, url.port() == -1 ? 0 : url.port());
	myHttpGetId = get(urlStr, file);
}



void HttpConnection::configurate() {
    std::cout << "http connecton configurate\n";
    Configurator configurator;

//typedef std::map<const std::string, std::string*> Map;
    Map settings;
    std::string name1("PROXY");
    std::string name2("PORT");
    std::string value1(" ");
    std::string value2(" ");
    settings.insert(std::make_pair(name1, &value1));
    settings.insert(std::make_pair(name2, &value2));
    configurator.setParameters(ourConfigFilePath.toStdString(), settings);
//    for (Map::const_iterator it = settings.begin(); it != settings.end(); ++it) {
  //      std::cout << it->first.c_str() << " = " << it->second->c_str() << "\n";
 //   }
    ourProxy = value1.c_str();
    QString str(value2.c_str());
    ourPort = str.toInt();
    std::cout << "proxy = " << ourProxy.toStdString().c_str() << "\n";
    std::cout << "port = " << ourPort << "\n";
}

