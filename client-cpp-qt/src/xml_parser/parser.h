#ifndef _PARSER_H_
#define _PARSER_H_

#include <QtXml>

#include <string>

#include "../model/book_author.h"
#include "../model/model.h"

class AtomHandler : public QXmlDefaultHandler {

public:
	AtomHandler(Model* model);
public:
	bool characters (const QString& strText);
	bool endElement (const QString&, const QString&, const QString& str);
	bool startElement (const QString& , const QString& , const QString& name, const QXmlAttributes& );
	
	
private:
	Model* myModel; 

	QString myStrText;
    QString* myNextAtomPage;
	bool myIsEntry;
	QString myTitle;
	QString myLanguage;
	QString mySummary;
	QString myAuthorsName;
	QString myAuthorsUri;	
	QString myBooksUri;
	QString myBooksLink;
};

class AtomParser {

public:
	AtomParser();
	
public:
	void parse(QFile* file, Model* model);
	const QString* getNextAtomPage() const;

private:
    QString* myNextAtomPage;
};

#endif //_PARSER_H_

