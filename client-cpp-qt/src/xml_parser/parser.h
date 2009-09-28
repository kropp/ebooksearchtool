#ifndef _PARSER_H_
#define _PARSER_H_

#include <QtXml>

class AtomHandler : public QXmlDefaultHandler {

public:
	bool characters (const QString& strText);
	bool endElement (const QString&, const QString&, const QString& str);
	
private:
	QString myStrText;
};

class AtomParser {

public:
	AtomParser();
	
public:
	void parse();
};

#endif //_PARSER_H_

