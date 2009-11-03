#ifndef _HTTP_CONNECTION_H_
#define _HTTP_CONNECTION_H_

#include <QHttp>

class QFile;

class HttpConnection : public QHttp {

private:
    static QString ourConfigFilePath;
    static QString ourProxy;
    static int ourPort;    
    static QString ourServer;

public:
	HttpConnection(QObject* parent);
	
public:
	void download(QString url, QIODevice* out);
    QString getServer() const;

private:
    void configurate(); //set static fields from the config-file. setSettings();
    
public:
	int myHttpGetId;
};


#endif //_HTTP_CONNECTION_H_
