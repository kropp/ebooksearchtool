#ifndef CATALOGMANAGER_H
#define CATALOGMANAGER_H

#include <QObject>
#include <QList>
#include <QStringList>
#include <QMap>

#include "linksinformation.h"
#include "linksextractiondownloader.h"

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

    void setDownloaderConnections();

signals:

    void catalogRootChanged();
    void catalogContentsParsed(Catalog* catalog);
    void currentCatalogChanged(Catalog* catalog);

    void goUpAvailabilityChanged(bool newValue);
    void goBackAvailabilityChanged(bool newValue);
    void goForwardAvailabilityChanged(bool newValue);

    void catalogsRecreated();

private slots:

    void finishedParsing(bool success, Catalog* catalog);
    void setLinksForComplexCatalogs(bool, LinksInformation* info);
    void recreateAllCatalogs();

private:

    void openCatalog(Catalog* catalog, bool addToBackHistory);
    void parseCatalogRoot();
    void parseCatalogContents(Catalog* catalog);

    void searchLinksForComplexCatalogs();
    void createCatalogs();
    void createDownloaders();
    void setConnections();

private:

    static CatalogManager instance;

    Catalog* myCatalogRequestedForOpening;
    Catalog* myCurrentCatalog;

    Catalog* myRootCatalog;
    QList<Catalog*> mySimpleCatalogs;
    Catalog* myNewCatalog;
    Catalog* myPopularCatalog;

    QList<Catalog*>* myBrowseBackHistory;
    QList<Catalog*>* myBrowseForwardHistory;
    QMap<QString, CatalogDownloader*> myDownloadersMap;

    QList<LinksExtractionDownloader*> myLinksExtractionDownloaders;

    int currentCatalogParseCyclesAwaited;
};

#endif // CATALOGMANAGER_H
