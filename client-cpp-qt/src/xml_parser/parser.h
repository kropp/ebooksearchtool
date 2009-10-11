#ifndef _PARSER_H_
#define _PARSER_H_

#include <QtXml>

#include <string>

#include "../model/book_author.h"
#include "../model/model.h"

class AtomHandler : public QXmlDefaultHandler {

public:
	AtomHandler(Model* model) : myModel(model), myIsEntry(false) {}

public:
	bool characters (const QString& strText);
	bool endElement (const QString&, const QString&, const QString& str);
	bool startElement (const QString& , const QString& , const QString& name, const QXmlAttributes& );
	
private:
	QString myStrText;
	Model* myModel; 
	bool myIsEntry;
	QString myTitle;
	QString myLanguage;
	QString mySummary;
	QString myAuthorsName;
	QString myAuthorsUri;	
	QString myBooksUri;	
};

class AtomParser {

public:
	AtomParser();
	
public:
	void parse(QFile* file, Model* model);
};

#endif //_PARSER_H_

