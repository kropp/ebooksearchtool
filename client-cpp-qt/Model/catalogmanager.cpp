#include "catalogmanager.h"
#include "book.h"
#include "catalog.h"
#include "bookdownloader.h"
#include "catalogdownloader.h"

static const QString FEEDBOOKS_ID = "www.feedbooks.com";
static const QString MANYBOOKS_ID = "www.manybooks.net";
static const QString LITRES_ID = "data.fbreader.org";
static const QString SMASHWORDS_ID = "www.smashwords.com";
static const QString BOOKSERVER_ID = "bookserver.archive.org";


CatalogManager CatalogManager::instance;

CatalogManager::CatalogManager()
{
    createCatalogs();
    parseCatalogRoot();
    myCurrentCatalog = myRootCatalog;
    myCatalogRequestedForOpening = myRootCatalog;

    myBrowseBackHistory = new QList<Catalog*>();
    myBrowseForwardHistory = new QList<Catalog*>();

    myDownloadersMap.insert(FEEDBOOKS_ID, new CatalogDownloader(FEEDBOOKS_ID));
    myDownloadersMap.insert(MANYBOOKS_ID, new CatalogDownloader(MANYBOOKS_ID));
    myDownloadersMap.insert(LITRES_ID, new CatalogDownloader(LITRES_ID));
    myDownloadersMap.insert(SMASHWORDS_ID, new CatalogDownloader(SMASHWORDS_ID));
    myDownloadersMap.insert(BOOKSERVER_ID, new CatalogDownloader(BOOKSERVER_ID));

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
    myRootCatalog = new Catalog(false, "Root", new UrlData("-", "-"));
    myRootCatalog->markAsParsed();

    Catalog* feedbooks = new Catalog(false, "FeedBooks", new UrlData("/publicdomain/catalog.atom", FEEDBOOKS_ID));
    Catalog* manybooks = new Catalog(false, "ManyBooks", new UrlData("/stanza/catalog/", MANYBOOKS_ID));
    Catalog* litres = new Catalog(false, "Litres", new UrlData("/catalogs/litres/", LITRES_ID));
    Catalog* smashwords = new Catalog(false, "SmashWords", new UrlData("/atom", SMASHWORDS_ID));

    Catalog* newBooks = new Catalog(false, "MULTY_SERVERS_TEST", new UrlData("/books/recent.atom", FEEDBOOKS_ID));
    newBooks->addChildUrl(new UrlData("/catalogs/litres/", LITRES_ID));


                                    //new UrlData("/stanza/catalog/", MANYBOOKS_ID));
    //newBooks->addChildUrl(new UrlData("/books/recent.atom", FEEDBOOKS_ID));

    myRootCatalog->addCatalogToCatalog(feedbooks);
    myRootCatalog->addCatalogToCatalog(manybooks);
    myRootCatalog->addCatalogToCatalog(litres);
    myRootCatalog->addCatalogToCatalog(smashwords);
    myRootCatalog->addCatalogToCatalog(newBooks);

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

    foreach (UrlData* urlData, list)
    {
        CatalogDownloader* downloader = myDownloadersMap.value(urlData->server);
        downloader->startDownloadingCatalog(urlData->url, catalog);
    }

}
