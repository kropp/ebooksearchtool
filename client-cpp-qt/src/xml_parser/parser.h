#ifndef _PARSER_H_
#define _PARSER_H_

#include <QtXml>

#include<iostream>

class AtomHandler : public QXmlDefaultHandler {

public:
	AtomHandler(QByteArray* output) : myBuffer(output), myIsEntry(false) {}

public:
	bool characters (const QString& strText);
	bool endElement (const QString&, const QString&, const QString& str);
	bool startElement (const QString& , const QString& , const QString& name, const QXmlAttributes& );
	
private:
	QString myStrText;
	QByteArray* myBuffer; 
	bool myIsEntry;
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

