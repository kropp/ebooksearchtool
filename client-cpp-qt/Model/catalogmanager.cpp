#include "catalogmanager.h"
#include "book.h"
#include "catalog.h"
#include "bookdownloader.h"
#include "catalogdownloader.h"

static const QString FEEDBOOKS_ID = "www.feedbooks.com";

static const QString MANYBOOKS_ID = "manybooks.net";

CatalogManager CatalogManager::instance;

CatalogManager::CatalogManager()
{
    createCatalogs();
    parseCatalogRoot();
    myCurrentCatalog = myRootCatalog;
    myCatalogRequestedForOpening = myRootCatalog;

    myBrowseBackHistory = new QList<Catalog*>();
    myBrowseForwardHistory = new QList<Catalog*>();

    //   myDownloadersMap.insert("www.feedbooks.com", new CatalogDownloader("www.feedbooks.com"));
    //  myDownloadersMap.insert("www.manybooks.net", new CatalogDownloader("www.manybooks.net"));

    myDownloadersMap.insert(FEEDBOOKS_ID, new CatalogDownloader(FEEDBOOKS_ID));
    myDownloadersMap.insert(MANYBOOKS_ID, new CatalogDownloader(MANYBOOKS_ID));

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

void CatalogManager::finishedParsing(bool success, Catalog* catalog)
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



            myCatalogRequestedForOpening = 0;
        }
    }
}

void CatalogManager::createCatalogs()
{
    myRootCatalog = new Catalog(false, "Root", new UrlData("-", "-"));
    myRootCatalog->markAsParsed();

    Catalog* feedbooks = new Catalog(false, "FeedBooks", new UrlData("/publicdomain/catalog.atom", "www.feedbooks.com"));

    Catalog* test = new Catalog(false, "Test", new UrlData("/publicdomain/catalog.atom", "www.feedbooks.com"));
    test->addChildUrl(new UrlData("/stanza/catalog/","manybooks.net"));

    //Catalog* test = new Catalog(false, "Test", new UrlData("/stanza/catalog/", "manybooks.net"));

    //Catalog* popular = new Catalog(false, "Popular", "/publicdomain/catalog.atom");

    //cat2 = new Catalog(true, "ManyBooks", "-");
    //cat3 = new Catalog(false, "LolBooks", "-");

    myRootCatalog->addCatalogToCatalog(feedbooks);
    myRootCatalog->addCatalogToCatalog(test);
    /*rootCatalog->addCatalogToCatalog(cat2);
    rootCatalog->addCatalogToCatalog(cat3);*/
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
    QList<UrlData*> list = catalog->getUrlList();

    currentCatalogParseCyclesAwaited = list.count();

    foreach (UrlData* urlData, list)
    {
        CatalogDownloader* downloader = myDownloadersMap.value(urlData->server);
        downloader->startDownloadingCatalog(urlData->url, catalog);
    }

    /*foreach(QString url, list) {
        // TODO
        //ищем в map подходящий загрузчик 
        //проверки
        myDownloader->startDownloadingCatalog(url, catalog);
    }
     // перебираю

    //TODO map  (QString - Downloader)   url->downloader
*/
}
