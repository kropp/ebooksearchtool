#ifndef _HANDLER_H_
#define _HANDLER_H_

#include <QtXml>
#include <QString>

#include "../author.h"
#include "../book.h"
#include "../search_result.h"
#include "opds_constants.h"

#include "../linksinformation.h"
#include "../catalog.h"

class Catalog;

class OPDSHandler : public QXmlDefaultHandler, public OPDSConstants {

public:

    OPDSHandler(QVector<Book*>* data, SearchResult& result);
    OPDSHandler(QVector<Book*>* bookData, QVector<Catalog*>* catalogData, QString parsedServer, SearchResult& result);
    OPDSHandler(LinksInformation* linksInfo);
    ~OPDSHandler();

private:

    bool characters (const QString& strText);
    bool endElement (const QString&, const QString&, const QString& str);
    bool startElement (const QString& , const QString& , const QString& name, const QXmlAttributes& );
    
    void parseCatalogLinks(const QXmlAttributes& attributes);
    void processLink(const QXmlAttributes& attributes);

    void startNewEntry();
    void endEntry();
	
private:

    QVector<Book*>* myBookData;
    QVector<Catalog*>* myCatalogData;
    Catalog* currentRootCatalog;

    QString currentParsedServer;
    
    QString myCurrentText;
    bool myIsEntry;
    bool myIsInContent;
    QString myOpdsCatalog;

    QString title;
    QString authorsName;
    QString authorsUri;
    QString entryId;
    Author* entryAuthor;
    QString summary;
    QString rights;
    QString updated;
    QString content;
    QString language;
    QString issued;
    QString publisher;
    QString coverLink;
    UrlData* catalogLinkUrl;

    QPair<QString, QString> myLocalLink;

    QVector<QString>* categories;
    QVector<Author*>* authors;
    QMap<QString, QString>* mySourceLinks;

    LinksInformation* myLinksInformation;

    bool myParseLinksMode;

    SearchResult* mySearchResult;
};

#endif //_HANDLER_H_
