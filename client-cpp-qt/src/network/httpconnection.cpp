#include <QtNetwork>

#include "httpconnection.h"
#include "../configurator/configurator.h"

#include <iostream>

QString HttpConnection::ourConfigFilePath = "../.config/network";


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
    QString name1 = "PROXY";
    QString name2 = "PORT";
    QString value1;
    QString value2;
    settings.insert(std::make_pair(name1.toStdString(), value1.toStdString()));
    settings.insert(std::make_pair(name2.toStdString(), value2.toStdString()));
    configurator.setParameters(ourConfigFilePath.toStdString(), settings);
    for (Map::const_iterator it = settings.begin(); it != settings.end(); ++it) {
        std::cout << it->first.c_str() << " = " << it->second.c_str() << "\n";
    }
}

