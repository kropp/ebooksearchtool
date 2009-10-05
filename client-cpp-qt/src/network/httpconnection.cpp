#include <QtNetwork>

#include "httpconnection.h"

void HttpConnection::downloadFile(QString urlStr, QFile* file) {
	QUrl url(urlStr);
	QHttp::ConnectionMode mode = url.scheme().toLower() == "https" ? QHttp::ConnectionModeHttps : QHttp::ConnectionModeHttp;
	setHost(url.host(), mode, url.port() == -1 ? 0 : url.port());
	myHttpGetId = get(urlStr, file);
}
