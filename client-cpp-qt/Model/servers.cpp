#include "servers.h"

static const QString FEEDBOOKS_ALIAS = "FeedBooks";
static const QString MANYBOOKS_ALIAS = "ManyBooks";
static const QString LITRES_ALIAS = "Litres";
static const QString SMASHWORDS_ALIAS = "SmashWords";
static const QString BOOKSERVER_ALIAS = "BookServer";
static const QString EBOOKSEARCH_ALIAS = "eBookSearch";

static const QString FEEDBOOKS_ID = "www.feedbooks.com";
static const QString MANYBOOKS_ID = "manybooks.net";
static const QString LITRES_ID = "data.fbreader.org";
static const QString SMASHWORDS_ID = "www.smashwords.com";
static const QString BOOKSERVER_ID = "bookserver.archive.org";
static const QString EBOOKSEARCH_ID = "ebooksearch.webfactional.com";

static const QString FEEDBOOKS_SEARCH_PATH = "/books/search.atom?query=";
static const QString MANYBOOKS_SEARCH_PATH = "/stanza/search.php?q=";
static const QString LITRES_SEARCH_PATH = "/catalogs/litres/search.php?q=";
static const QString SMASHWORDS_SEARCH_PATH = "/atom/search/books?query=";
static const QString BOOKSERVER_SEARCH_PATH = "/catalog/opensearch?q=";
static const QString EBOOKSEARCH_SEARCH_PATH = "/search?query=";

static const QString FEEDBOOKS_ATOM_PATH = "/publicdomain/catalog.atom";
static const QString MANYBOOKS_ATOM_PATH = "/stanza/catalog/";
static const QString LITRES_ATOM_PATH = "/catalogs/litres/";
static const QString SMASHWORDS_ATOM_PATH = "/atom";
static const QString BOOKSERVER_ATOM_PATH = "/catalog/";
static const QString EBOOKSEARCH_ATOM_PATH = "/catalog.atom";

EBookSearchTool EBookSearchTool::instance;

EBookSearchTool::EBookSearchTool()
{
    initializeServers();
}

EBookSearchTool* EBookSearchTool::getInstance()
{
    return &instance;
}

void EBookSearchTool::deleteServer(QString serverHost)
{
    ourServers.remove(serverHost);
    emit serversChanged();
}

const QMap<QString, ServerInfo*>& EBookSearchTool::getServers()
{
    return ourServers;
}

//    mySimpleCatalogs.append(new Catalog(false, "BookServer", new UrlData("/catalog/", BOOKSERVER_ID)));

void EBookSearchTool::initializeServers() {
    ourServers.clear();
    addServerWithNotification(FEEDBOOKS_ALIAS, FEEDBOOKS_ID, FEEDBOOKS_SEARCH_PATH, FEEDBOOKS_ATOM_PATH, true, true, false);
    addServerWithNotification(LITRES_ALIAS, LITRES_ID, LITRES_SEARCH_PATH, LITRES_ATOM_PATH, true, true, false);
    addServerWithNotification(BOOKSERVER_ALIAS, BOOKSERVER_ID, BOOKSERVER_SEARCH_PATH, BOOKSERVER_ATOM_PATH, true, true, false);
    addServerWithNotification(SMASHWORDS_ALIAS, SMASHWORDS_ID, SMASHWORDS_SEARCH_PATH, SMASHWORDS_ATOM_PATH, true, true, false);
    addServerWithNotification(EBOOKSEARCH_ALIAS, EBOOKSEARCH_ID, EBOOKSEARCH_SEARCH_PATH, EBOOKSEARCH_ATOM_PATH, true, true, false);
    addServerWithNotification(MANYBOOKS_ALIAS, MANYBOOKS_ID, MANYBOOKS_SEARCH_PATH, MANYBOOKS_ATOM_PATH, true, true, false);
    emit serversChanged();
}

void EBookSearchTool::dropServersToDefault()
{
    initializeServers();
}


void EBookSearchTool::addServer(QString alias, QString serverPath, QString searchPath, QString atomPath, bool bookSearchInclude, bool catalogSearchInclude)
{
    addServerWithNotification(alias, serverPath, searchPath, atomPath, bookSearchInclude, catalogSearchInclude, true);
}

void EBookSearchTool::addServerWithNotification(QString alias, QString serverPath, QString searchPath, QString atomPath, bool bookSearchInclude, bool catalogSearchInclude, bool emitSignal)
{
    ServerInfo* info = new ServerInfo();

    info->ProgramAlias = alias;
    info->ServerPath = serverPath;
    info->SearchPath = searchPath;
    info->RootAtomPath = atomPath;
    info->includedInBookSearch = bookSearchInclude;
    info->includedInCatalogSearch = catalogSearchInclude;

    ourServers.insert(serverPath, info);

    if (emitSignal)
    {
        emit serversChanged();
    }
}
