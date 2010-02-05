#ifndef _HANDLER_H_
#define _HANDLER_H_

#include <QtXml>
#include <QString>

#include "../data/book_author.h"
#include "../data/data.h"

class AtomHandler : public QXmlDefaultHandler {

private: 
    static QString ourConfigFilePath;

private:
	AtomHandler(Data* data);
	
private:
	bool characters (const QString& strText);
	bool endElement (const QString&, const QString&, const QString& str);
	bool startElement (const QString& , const QString& , const QString& name, const QXmlAttributes& );
	
private:
	Data* myData; 

	QString myCurrentText;
    QString* myNextAtomPage;
	bool myIsEntry;
	QString myTitle;
	QString myLanguage;
	QString mySummary;
	QString myAuthorsName;
	QString myAuthorsUri;	
	QString myBooksUri;
	QString myBooksLink;
    QString myBooksCover;
    QString myFormat;

friend class AtomParser;
};

#endif //_HANDLER_H_


