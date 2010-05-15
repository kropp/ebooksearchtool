#include <QDebug>

#include "catalogmanager.h"
#include "book.h"
#include "catalog.h"
#include "bookdownloader.h"
#include "catalogdownloader.h"
#include "opds_parser/opds_constants.h"

#include "linksextractiondownloader.h"
#include "settings.h"


CatalogManager CatalogManager::instance;

CatalogManager::CatalogManager()
{
    createCatalogs();
    parseCatalogRoot();
    myCurrentCatalog = myRootCatalog;
    myCatalogRequestedForOpening = myRootCatalog;

    myBrowseBackHistory = new QList<Catalog*>();
    myBrowseForwardHistory = new QList<Catalog*>();

    createDownloaders();

    setConnections();

    currentCatalogParseCyclesAwaited = 0;
}

CatalogManager* CatalogManager::getInstance()
{
    return &instance;
}

void CatalogManager::goUpLevel()
{
    Catalog* upLevelCatalog = myCurrentCatalog->getParent();
    openCatalog(upLevelCatalog);
}

void CatalogManager::goBack()
{
    if (goBackAvailable())
    {
        Catalog* backCatalog = myBrowseBackHistory->last();

        myBrowseBackHistory->removeLast();
        myBrowseForwardHistory->append(myCurrentCatalog);

        openCatalog(backCatalog, false);

        emit goBackAvailabilityChanged(goBackAvailable());
        emit goForwardAvailabilityChanged(goForwardAvailable());
    }
}

void CatalogManager::goForward()
{
    if (goForwardAvailable())
    {
        Catalog* forwardCatalog = myBrowseForwardHistory->last();

        myBrowseForwardHistory->removeLast();
        myBrowseBackHistory->append(myCurrentCatalog);

        openCatalog(forwardCatalog, false);

        emit goBackAvailabilityChanged(goBackAvailable());
        emit goForwardAvailabilityChanged(goForwardAvailable());
    }
}

bool CatalogManager::goBackAvailable()
{
    return myBrowseBackHistory->size() > 0;
}

bool CatalogManager::goForwardAvailable()
{
    return myBrowseForwardHistory->size() > 0;
}

bool CatalogManager::goUpAvailable()
{
    return myCurrentCatalog->getParent() != 0;
}

void CatalogManager::openRoot()
{
    openCatalog(myRootCatalog, true);
}

void CatalogManager::openCatalog(Catalog* catalog)
{
    openCatalog(catalog, true);
}

void CatalogManager::createDownloaders() {
    const Settings& settings = Settings::getInstance();
    const QStringList& servers = settings.getServers();

    qDebug() << "CatalogManager::createDownloaders() servers = " <<  servers;

    foreach (QString server, servers) {
        myDownloadersMap.insert(server, new CatalogDownloader(server));
    }
}


void CatalogManager::openCatalog(Catalog* catalog, bool addToBackHistory)
{
    if (catalog == myCatalogRequestedForOpening) {
        return;
    }
    myCatalogRequestedForOpening = catalog;

    if (!catalog->isCatalogParsed())
    {
        parseCatalogContents(catalog);
    }
    else
    {
        if (addToBackHistory)
        {
            myBrowseBackHistory->append(myCurrentCatalog);
            myBrowseForwardHistory->clear();
        }

        myCurrentCatalog = catalog;

        emit goBackAvailabilityChanged(goBackAvailable());
        emit goForwardAvailabilityChanged(goForwardAvailable());
        emit goUpAvailabilityChanged(goUpAvailable());
        emit currentCatalogChanged(myCurrentCatalog);
    }
}

void CatalogManager::setConnections()
{
    QList<CatalogDownloader*> values = myDownloadersMap.values();

    foreach (CatalogDownloader* downloader, values)
    {
        connect(downloader, SIGNAL(downloadFinished(bool,Catalog*)), this, SLOT(finishedParsing(bool, Catalog*)));
    }
}

void CatalogManager::finishedParsing(bool /*success*/, Catalog* catalog)
{
    if (myCatalogRequestedForOpening == catalog)
    {
        currentCatalogParseCyclesAwaited--;

        if (currentCatalogParseCyclesAwaited == 0)
        {

            myBrowseBackHistory->append(myCurrentCatalog);
            myBrowseForwardHistory->clear();
            myCurrentCatalog = catalog;

            emit goBackAvailabilityChanged(goBackAvailable());
            emit goForwardAvailabilityChanged(goForwardAvailable());
            emit goUpAvailabilityChanged(goUpAvailable());
            emit currentCatalogChanged(myCurrentCatalog);
        }
    }
}

void CatalogManager::createCatalogs()
{
    qDebug() << "CatalogManager::createCatalogs ";
    myRootCatalog = new Catalog(false, "Root", new UrlData("-", "-"));
    myRootCatalog->markAsParsed();

    mySimpleCatalogs.append(new Catalog(false, "FeedBooks", new UrlData("/publicdomain/catalog.atom", FEEDBOOKS_ID)));
    mySimpleCatalogs.append(new Catalog(false, "ManyBooks", new UrlData("/stanza/catalog/", MANYBOOKS_ID)));
    mySimpleCatalogs.append(new Catalog(false, "Litres", new UrlData("/catalogs/litres/", LITRES_ID)));
    mySimpleCatalogs.append(new Catalog(false, "SmashWords", new UrlData("/atom", SMASHWORDS_ID)));
    mySimpleCatalogs.append(new Catalog(false, "eBookSearch", new UrlData("/catalog.atom", EBOOKSEARCH_ID)));
    mySimpleCatalogs.append(new Catalog(false, "BookServer", new UrlData("/catalog/", BOOKSERVER_ID)));


    myNewCatalog = new Catalog(false, tr("NEW"), tr("New books from all the servers"), 0);

    myPopularCatalog = new Catalog(false, tr("POPULAR"), tr("Popular books from all the servers"), 0);

    myRootCatalog->addCatalogToCatalog(myNewCatalog);
    myRootCatalog->addCatalogToCatalog(myPopularCatalog);

    foreach (Catalog* catalog, mySimpleCatalogs) {
        myRootCatalog->addCatalogToCatalog(catalog);
    }

    searchLinksForComplexCatalogs();
}


void CatalogManager::searchLinksForComplexCatalogs() {
    foreach (Catalog* catalog, mySimpleCatalogs) {
        const QList<UrlData*>& urlList = catalog->getUrlList();
        myLinksExtractionDownloaders.append(new LinksExtractionDownloader(urlList.at(0)->server, urlList.at(0)->url));
    }

    foreach (LinksExtractionDownloader* downloader, myLinksExtractionDownloaders) {
        connect(downloader, SIGNAL(downloadFinished(bool,LinksInformation*)), this, SLOT(setLinksForComplexCatalogs(bool, LinksInformation*)));
        downloader->startExtractingLinks();
    }
}

void CatalogManager::setLinksForComplexCatalogs(bool, LinksInformation* linksInfo) {
//     qDebug() << "CatalogManager::setLinksForComplexCatalogs() links "
//             << linksInfo->getNewLinks() << linksInfo->getPolularLinks();

      const QStringList& newLinks = linksInfo->getNewLinks();
      foreach (QString link, newLinks) {
          myNewCatalog->addChildUrl(link);
      }

      const QStringList& popularLinks = linksInfo->getPolularLinks();
      foreach (QString link, popularLinks) {
          myPopularCatalog->addChildUrl(link);
      }
}

Catalog* CatalogManager::getCatalogRoot()
{
    return myRootCatalog;
}

void CatalogManager::parseCatalogRoot()
{
    emit catalogRootChanged();
}

void CatalogManager::parseCatalogContents(Catalog* catalog)
{
    const QList<UrlData*>& list = catalog->getUrlList();


    currentCatalogParseCyclesAwaited = list.count();
    if (currentCatalogParseCyclesAwaited == 0)
    {
        finishedParsing(true, catalog);
    }


    foreach (UrlData* urlData, list)
    {
        CatalogDownloader* downloader = myDownloadersMap.value(urlData->server);
        downloader->startDownloadingCatalog(urlData->url, catalog);
    }

}
