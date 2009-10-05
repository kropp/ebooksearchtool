#ifndef _HTTP_CONNECTION_H_
#define _HTTP_CONNECTION_H_

#include <QHttp>

class QFile;

class HttpConnection : public QHttp {

public:
	HttpConnection(QObject* parent = 0) : QHttp(parent) {}
	
public:
	void downloadFile(QString url, QFile* file);

public:
	int myHttpGetId;
};


#endif //_HTTP_CONNECTION_H_
