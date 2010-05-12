#include "catalog.h"
#include "book.h"

#include <QDebug>

Catalog::Catalog(bool isBookCatalogFlag, QString catalogName, QString catalogSummary, UrlData* newUrl)
{
    myIsBookCatalog = isBookCatalogFlag;
    myName = catalogName;
    mySummary = catalogSummary;
    myUrlList.append(newUrl);
    myIsParsed = false;
    myParent = 0;

//    if (myIsBookCatalog)
//    {
//        myChildBooks = new QVector<Book*>();
//        myChildCatalogs = 0;
//    }
//    else
//    {
//        myChildCatalogs = new QVector<Catalog*>();
//        myChildBooks = 0;
//    }
}

Catalog::Catalog(bool isBookCatalogFlag, QString catalogName, UrlData* newUrl)
{
    myIsBookCatalog = isBookCatalogFlag;
    myName = catalogName;
    myUrlList.append(newUrl);
    myIsParsed = false;
    myParent = 0;

//    if (myIsBookCatalog)
//    {
//        myChildBooks = new QVector<Book*>();
//    }
//    else
//    {
//        myChildCatalogs = new QVector<Catalog*>();
//    }
}


QList<UrlData*> Catalog::getUrlList()
{
    return myUrlList;
}

Catalog::Catalog(QString catalogName, UrlData* newUrl)
{
    myName = catalogName;
    myUrlList.append(newUrl);
    myIsParsed = false;
    myParent = 0;
//    myChildBooks = 0;
//    myChildCatalogs = 0;
}

const QString& Catalog::getSummary() {
    return mySummary;
}


void Catalog::addChildUrl(UrlData* newUrl) {
    myUrlList.append(newUrl);
}


void Catalog::setCatalogType(bool isBookCatalogFlag)
{
    myIsBookCatalog = isBookCatalogFlag;

//    if (myIsBookCatalog)
//    {
//        if (myChildBooks == 0)
//        {
//            myChildBooks = new QVector<Book*>();
//        }
//    }
//    else
//    {
//        if (myChildCatalogs == 0)
//        {
//            myChildCatalogs = new QVector<Catalog*>();
//        }
//    }
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
