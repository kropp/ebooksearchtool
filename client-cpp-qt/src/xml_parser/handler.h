#ifndef _HANDLER_H_
#define _HANDLER_H_

#include <QtXml>
#include <QString>

#include "../model/book_author.h"
#include "../model/model.h"

class AtomHandler : public QXmlDefaultHandler {

private:
	AtomHandler(Model* model);
	
private:
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

friend class AtomParser;
};

#endif //_HANDLER_H_

