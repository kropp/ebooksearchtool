#include <QDebug>

#include "catalogmanager.h"
#include "book.h"
#include "catalog.h"
#include "bookdownloader.h"
#include "catalogdownloader.h"
#include "opds_parser/opds_constants.h"

#include "linksextractiondownloader.h"
#include "servers.h"

CatalogManager CatalogManager::instance;

CatalogManager::CatalogManager()
{
    setConnections();
    recreateAllCatalogs();
}

CatalogManager* CatalogManager::getInstance()
{
    return &instance;
}

void CatalogManager::setConnections()
{
    connect(EBookSearchTool::getInstance(), SIGNAL(serversChanged()), this, SLOT(recreateAllCatalogs()));
}


void CatalogManager::recreateAllCatalogs()
{
    mySimpleCatalogs.clear();
    myDownloadersMap.clear();
    myLinksExtractionDownloaders.clear();

    createDownloaders();

    setDownloaderConnections();

    createCatalogs();
    parseCatalogRoot();
    myCurrentCatalog = myRootCatalog;
    myCatalogRequestedForOpening = myRootCatalog;

    currentCatalogParseCyclesAwaited = 0;

    emit catalogsRecreated();
    emit currentCatalogChanged(myRootCatalog);
    emit goBackAvailabilityChanged(goBackAvailable());
    emit goForwardAvailabilityChanged(goForwardAvailable());
    emit goUpAvailabilityChanged(goUpAvailable());
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
        Catalog* backCatalog = myBrowseBackHistory.last();

        myBrowseBackHistory.removeLast();
        myBrowseForwardHistory.append(myCurrentCatalog);

        openCatalog(backCatalog, false);

        emit goBackAvailabilityChanged(goBackAvailable());
        emit goForwardAvailabilityChanged(goForwardAvailable());
    }
}

void CatalogManager::goForward()
{
    if (goForwardAvailable())
    {
        Catalog* forwardCatalog = myBrowseForwardHistory.last();

        myBrowseForwardHistory.removeLast();
        myBrowseBackHistory.append(myCurrentCatalog);

        openCatalog(forwardCatalog, false);

        emit goBackAvailabilityChanged(goBackAvailable());
        emit goForwardAvailabilityChanged(goForwardAvailable());
    }
}

bool CatalogManager::goBackAvailable()
{
    return myBrowseBackHistory.size() > 0;
}

bool CatalogManager::goForwardAvailable()
{
    return myBrowseForwardHistory.size() > 0;
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
    foreach (ServerInfo* serverInfo, EBookSearchTool::getInstance()->getServers())
    {
        myDownloadersMap.insert(serverInfo->ServerPath, new CatalogDownloader(serverInfo->ServerPath));
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
        emit startedOpening();
        parseCatalogContents(catalog);        
    }
    else
    {
        if (addToBackHistory)
        {
            myBrowseBackHistory.append(myCurrentCatalog);
            myBrowseForwardHistory.clear();
        }

        myCurrentCatalog = catalog;

        emit goBackAvailabilityChanged(goBackAvailable());
        emit goForwardAvailabilityChanged(goForwardAvailable());
        emit goUpAvailabilityChanged(goUpAvailable());
        emit currentCatalogChanged(myCurrentCatalog);
    }
}

void CatalogManager::setDownloaderConnections()
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

            myBrowseBackHistory.append(myCurrentCatalog);
            myBrowseForwardHistory.clear();
            myCurrentCatalog = catalog;

            emit finishedOpening();
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

    foreach (ServerInfo* serverInfo, EBookSearchTool::getInstance()->getServers())
    {
        if (serverInfo->includedInCatalogSearch)
        {
            mySimpleCatalogs.append(new Catalog(false, serverInfo->ProgramAlias, new UrlData(serverInfo->RootAtomPath, serverInfo->ServerPath)));
        }
    }

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
    qDebug() << "CatalogManager::searchLinksForComplexCatalogs() ";
    foreach (Catalog* catalog, mySimpleCatalogs) {
        const QList<UrlData*>& urlList = catalog->getUrlList();
        myLinksExtractionDownloaders.append(new LinksExtractionDownloader(urlList.at(0)->server, urlList.at(0)->url));
    }

    foreach (LinksExtractionDownloader* downloader, myLinksExtractionDownloaders) {
        connect(downloader, SIGNAL(downloadFinished(bool,LinksInformation*)), this, SLOT(setLinksForComplexCatalogs(bool, LinksInformation*)));
        downloader->startExtractingLinks();
    }
}

void CatalogManager::setLinksForComplexCatalogs(bool success, LinksInformation* linksInfo) {
    if (!success) {
        return;
    }
    qDebug() << "CatalogManager::setLinksForComplexCatalogs() links "
             << linksInfo->getNewLinks() << linksInfo->getPolularLinks();

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
