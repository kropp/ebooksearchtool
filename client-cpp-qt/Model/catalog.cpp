#include "catalog.h"
#include "book.h"

Catalog::Catalog(bool isBookCatalogFlag, QString catalogName, QString catalogSummary, UrlData* newUrl)
{
    myIsBookCatalog = isBookCatalogFlag;
    myName = catalogName;
    mySummary = catalogSummary;
    myUrlList.append(newUrl);
    myIsParsed = false;
    myParent = 0;

    if (myIsBookCatalog)
    {
        m_childBooks = new QVector<Book*>();
    }
    else
    {
        m_childCatalogs = new QVector<Catalog*>();
    }
}

Catalog::Catalog(bool isBookCatalogFlag, QString catalogName, UrlData* newUrl)
{
    myIsBookCatalog = isBookCatalogFlag;
    myName = catalogName;
    myUrlList.append(newUrl);
    myIsParsed = false;
    myParent = 0;

    if (myIsBookCatalog)
    {
        m_childBooks = new QVector<Book*>();
    }
    else
    {
        m_childCatalogs = new QVector<Catalog*>();
    }
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
    m_childBooks = 0;
    m_childCatalogs = 0;
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

    if (myIsBookCatalog)
    {
        if (m_childBooks == 0)
        {
            m_childBooks = new QVector<Book*>();
        }
    }
    else
    {
        if (m_childCatalogs == 0)
        {
            m_childCatalogs = new QVector<Catalog*>();
        }
    }
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

QVector<Catalog*>* Catalog::getCatalogs()
{
    return m_childCatalogs;
}

QVector<Book*>* Catalog::getBooks()
{
    return m_childBooks;
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
        m_childBooks->append(newBook);
    }
}

void Catalog::addCatalogToCatalog(Catalog* newCatalog)
{
    if (!myIsBookCatalog)
    {
        m_childCatalogs->append(newCatalog);
        newCatalog->setParent(this);
    }
}

const QString& Catalog::getCatalogName()
{
    return myName;
}
