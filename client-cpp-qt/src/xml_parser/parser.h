#ifndef _PARSER_H_
#define _PARSER_H_

#include <QtXml>

#include<iostream>

class AtomHandler : public QXmlDefaultHandler {

public:
	AtomHandler(QByteArray* output) : myBuffer(output) {}

public:
	bool characters (const QString& strText);
	bool endElement (const QString&, const QString&, const QString& str);
	
private:
	QString myStrText;
	QByteArray* myBuffer; 
};

class AtomParser {

public:
	AtomParser();
	
public:
	void parse(QFile* file);
	void setOutput(QByteArray* buffer);

private:
	QByteArray* myOutputBuffer;
};

#endif //_PARSER_H_

