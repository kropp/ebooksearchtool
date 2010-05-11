#ifndef CATALOGMANAGER_H
#define CATALOGMANAGER_H

#include <QObject>
#include <QList>
#include <QMap>

class Catalog;
class BookDownloader;
class CatalogDownloader;

class CatalogManager : public QObject
{

    Q_OBJECT

private:

    CatalogManager();

public:

    static CatalogManager* getInstance();

public:

    Catalog* getCatalogRoot();
    void openRoot();
    void goUpLevel();
    void goBack();
    void goForward();
    void openCatalog(Catalog* catalog);
    bool goBackAvailable();
    bool goForwardAvailable();
    bool goUpAvailable();

    void setConnections();

signals:

    void catalogRootChanged();
    void catalogContentsParsed(Catalog* catalog);
    void currentCatalogChanged(Catalog* catalog);

    void goUpAvailabilityChanged(bool newValue);
    void goBackAvailabilityChanged(bool newValue);
    void goForwardAvailabilityChanged(bool newValue);

private slots:

    void finishedParsing(bool success, Catalog* catalog);

private:

    void openCatalog(Catalog* catalog, bool addToBackHistory);
    void parseCatalogRoot();
    void parseCatalogContents(Catalog* catalog);

    void createCatalogs();

private:

    static CatalogManager instance;

    Catalog* myCatalogRequestedForOpening;
    Catalog* myCurrentCatalog;

    Catalog* myRootCatalog;

    QList<Catalog*>* myBrowseBackHistory;
    QList<Catalog*>* myBrowseForwardHistory;

    QMap<QString, CatalogDownloader*> myDownloadersMap;

    int currentCatalogParseCyclesAwaited;
};

#endif // CATALOGMANAGER_H
