#include "catalog.h"
#include "book.h"
#include "servers.h"

#include <QDebug>

Catalog::Catalog(bool isBookCatalogFlag, QString catalogName, QString catalogSummary, UrlData* newUrl)
{
    myIsBookCatalog = isBookCatalogFlag;
    myName = catalogName;
    mySummary = catalogSummary;
    if (newUrl) {
        myUrlList.append(newUrl);
    }
    myIsParsed = false;
    myParent = 0;
}

Catalog::Catalog(bool isBookCatalogFlag, QString catalogName, UrlData* newUrl)
{
    myIsBookCatalog = isBookCatalogFlag;
    myName = catalogName;
    if (newUrl) {
        myUrlList.append(newUrl);
    }
    myIsParsed = false;
    myParent = 0;

}


const QList<UrlData*>& Catalog::getUrlList()
{
    return myUrlList;
}

Catalog::Catalog(QString catalogName, UrlData* newUrl)
{
    myName = catalogName;
    if (newUrl) {
        myUrlList.append(newUrl);
    }
    myIsParsed = false;
    myParent = 0;
}

const QString& Catalog::getSummary() {
    return mySummary;
}


void Catalog::addChildUrl(UrlData* newUrl) {
    if (!newUrl) {
        return;
    }
    if (!containServer(newUrl->server)) {
        myUrlList.append(newUrl);
    }
}

void Catalog::addChildUrl(QString url) {
    url.remove("http://");
    foreach (ServerInfo* serverInfo, EBookSearchTool::getInstance()->getServers()) {

        QString server = serverInfo->ServerPath;

        QString part(server);
        part.remove("www.");
        if (url.contains(part)) {
            url.remove(part);
            url.remove("www.");
            UrlData* newUrl = new UrlData(url, server);
            qDebug() << "Catalog::addChildUrl for complex catalog" << url << "  " << server;
            addChildUrl(newUrl);
            break;
        }
    }

}


void Catalog::setCatalogType(bool isBookCatalogFlag)
{
    myIsBookCatalog = isBookCatalogFlag;


}

void Catalog::setParent(Catalog* catalog)
{
    myParent = catalog;
}

bool Catalog::isCatalogParsed()
{
    return myIsParsed;
}

void Catalog::markAsParsed()
{
    myIsParsed = true;
}

bool Catalog::containsBooks()
{
    return myIsBookCatalog;
}

QVector<Catalog*>& Catalog::getCatalogs()
{
    return myChildCatalogs;
}

QVector<Book*>& Catalog::getBooks()
{
    return myChildBooks;
}

Catalog* Catalog::getParent()
{
    return myParent;
}

bool Catalog::hasParent()
{
    return (myParent != 0);
}

void Catalog::addBookToCatalog(Book* newBook)
{
    if (myIsBookCatalog)
    {
        myChildBooks.append(newBook);
    }
}

void Catalog::addCatalogToCatalog(Catalog* newCatalog)
{
    if (!myIsBookCatalog)
    {
        myChildCatalogs.append(newCatalog);
        newCatalog->setParent(this);
    }
}

const QString& Catalog::getCatalogName()
{
    return myName;
}

bool Catalog::containServer(const QString& server) {
    bool contain = false;
    foreach (UrlData* url, myUrlList) {
        if (url->server == server) {
            contain = true;
            break;

        }
    }
    return contain;
}
