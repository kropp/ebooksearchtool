#include <QtNetwork>

#include "httpconnection.h"

HttpConnection::HttpConnection(QObject* parent) : QHttp(parent) {
}

void HttpConnection::setProxy(QString proxy, int port) {
	setProxy(proxy, port);
}

void HttpConnection::downloadFile(QString urlStr, QFile* file) {
	QUrl url(urlStr);
	QHttp::ConnectionMode mode = url.scheme().toLower() == "https" ? QHttp::ConnectionModeHttps : QHttp::ConnectionModeHttp;
	setHost(url.host(), mode, url.port() == -1 ? 0 : url.port());
	myHttpGetId = get(urlStr, file);
}
