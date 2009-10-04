#ifndef _HTTP_CONNECTION_H_
#define _HTTP_CONNECTION_H_

#include <QHttp>

class QFile;

class HttpConnection : public QHttp {

public:
	HttpConnection(QObject* parent = 0);
	
public:
	void downloadFile(QString url, QFile* file);
	void setProxy(QString proxy, int port);

public:
	int myHttpGetId;
};


#endif //_HTTP_CONNECTION_H_
