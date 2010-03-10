#ifndef _HANDLER_H_
#define _HANDLER_H_

#include <QtXml>
#include <QString>

#include "../book_author.h"
#include "../data.h"
#include "../search_result.h"
#include "opds_constants.h"
#include "XMLInputSource.h"

class OPDSHandler : public QXmlDefaultHandler, public OPDSConstants {

public:
	OPDSHandler(Data* data, SearchResult& result);
	//OPDSHandler(Data* data, const XMLInputSource* source);
   
   ~OPDSHandler();

private:
	bool characters (const QString& strText);
	bool endElement (const QString&, const QString&, const QString& str);
	bool startElement (const QString& , const QString& , const QString& name, const QXmlAttributes& );
    
    void setInitialValues();
    void processLink(const QXmlAttributes& attributes);
	
private:
	Data* myData; 
    Book* myBook;
    //const XMLInputSource* myInputFile;
    
    QString myCurrentText;
	bool myIsEntry;
	bool myIsInContent;
	QString myAuthorsName;
	QString myAuthorsUri;	
    QString myFormat;
    QString myOpdsCatalog;

    SearchResult& mySearchResult;
};

#endif //_HANDLER_H_
