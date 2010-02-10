#ifndef _HANDLER_H_
#define _HANDLER_H_

#include <QtXml>
#include <QString>

#include "../book_author.h"
#include "../data.h"

class OPDSHandler : public QXmlDefaultHandler {

public:
	OPDSHandler(Data* data);
    ~OPDSHandler();

private:
	bool characters (const QString& strText);
	bool endElement (const QString&, const QString&, const QString& str);
	bool startElement (const QString& , const QString& , const QString& name, const QXmlAttributes& );
    
    void setInitialValues();
    void processLink(const QXmlAttributes& attributes);
	
private:
	Data* myData; 

	QString myCurrentText;
	bool myIsEntry;
	bool myIsInContent;
	QString myTitle;
	QString myLanguage;
	QString mySummary;
	QString myAuthorsName;
	QString myAuthorsUri;	
	QString myBooksUri;
	QString myBooksLink;
    QString myBooksCover;
    QString myFormat;

//friend class AtomParser;
};

#endif //_HANDLER_H_


