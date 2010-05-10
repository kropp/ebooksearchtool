#ifndef CATALOG_H
#define CATALOG_H

#include<QStringList>
#include <QObject>
#include <QVector>

class Book;

struct UrlData
{
    UrlData(const QString& _url, const QString& _server)
    {
        url = _url;
        server = _server;
    }

    QString url;
    QString server;
};

class Catalog : QObject {

    Q_OBJECT

public:

    Catalog(QString catalogName, UrlData* newUrl);
    Catalog(bool isBookCatalogFlag, QString catalogName, UrlData* newUrl);

public:

    bool containsBooks();
    QVector<Catalog*>* getCatalogs();
    QVector<Book*>* getBooks();
    QString getCatalogName();

    Catalog* getParent();
    bool hasParent();

    void setParent(Catalog* catalog);
    void addBookToCatalog(Book* newBook);
    void addCatalogToCatalog(Catalog* newCatalog);
    void markAsParsed();
    void setCatalogType(bool isBookCatalogFlag);

    bool isCatalogParsed();

    QList<UrlData*> getUrlList();
    void addChildUrl(UrlData* newUrl);

private:

    Catalog* myParent;

    QVector<Catalog*>* m_childCatalogs;
    QVector<Book*>* m_childBooks;

    bool myIsBookCatalog;
    QString myName;

    QList<UrlData*> myUrlList;
    bool myIsParsed;
};

#endif // CATALOG_H
