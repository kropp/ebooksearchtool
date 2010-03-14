#include <QSettings>
#include <QString>
#include <QHttp>
#include <QProgressBar>
#include <QUrl>
#include <QDebug>

#include "networkmanager.h"

static const QString SERVER_FEEDBOOKS = "feedbooks.com";
static const QString OPENSEARCH_FEEDBOOKS = "/books/search.atom?query=";
static const QString SERVER_BOOKSERVER = "bookserver.archive.org";
static const QString OPENSEARCH_BOOKSERVER = "/catalog/opensearch?q=";
static const QString SERVER_SMASHWORDS = "smashwords.com";
static const QString OPENSEARCH_SMASHWORDS = "/atom/search/books?query=";
static const QString SERVER_MANYBOOKS = "manybooks.net";
static const QString OPENSEARCH_MANYBOOKS = "/stanza/search.php?q=";
static const QString SERVER_ONLY_MAWHRIN = "only.mawhrin.net";
static const QString OPENSEARCH_ONLY_MAWHRIN = "/ebooks/search.atom?title=";
static const QString SERVER_MUNSEYS = "catalog.lexcycle.com";
static const QString OPENSEARCH_MUNSEYS = "/munseys/op/search?search=";
 

NetworkManager* NetworkManager::instance = 0;

QString NetworkManager::ourConfigFilePath = "../.config.ini";
QString NetworkManager::ourProxy = "undefined";
int NetworkManager::ourPort = 80;    
QString NetworkManager::ourCurrentServer = "undefined";
QMap<QString, QString> NetworkManager::ourServersSearchSchema;

NetworkManager* NetworkManager::getInstance() {
    if (instance == 0) {
        instance = new NetworkManager();
    }
    return instance;
}

void NetworkManager::setServer(const QString& newServer) {
    ourCurrentServer= newServer;
}

NetworkManager::NetworkManager() {
    initializeMap();
    myHttpConnection = new QHttp(this);
    myConnectionForCovers = new QHttp(this);
    readSettings();
    connect(myHttpConnection, SIGNAL(requestFinished(int, bool)), this, SIGNAL(requestFinished(int, bool)));
    connect(myConnectionForCovers, SIGNAL(requestFinished(int, bool)), this, SIGNAL(coverRequestFinished(int, bool)));
    connect(myHttpConnection, SIGNAL(dataReadProgress(int, int)), this, SIGNAL(dataReadProgress(int, int)));
    connect(myHttpConnection, SIGNAL(stateChanged(int)), this, SLOT(showConnectionState(int)));
}

NetworkManager::~NetworkManager() {
    writeSettings();
}

QString NetworkManager::errorString() const {
  //  return myHttpConnection->errorString();
    return myHttpConnection->errorString();
}

void NetworkManager::readSettings() {
    QSettings settings(ourConfigFilePath, QSettings::IniFormat);
    ourProxy = settings.value("network/proxy").toString();
    ourPort = settings.value("network/port").toInt();
    ourCurrentServer= settings.value("network/server").toString();
}

void NetworkManager::writeSettings() const {
    QSettings settings(ourConfigFilePath, QSettings::IniFormat);
    settings.beginGroup("network");
    settings.setValue("proxy", ourProxy);
    settings.setValue("port", ourPort);
    settings.setValue("server", ourCurrentServer);
    settings.endGroup();
} 

// query - text from search line
int NetworkManager::download(QString query, QIODevice* out) {
    myOldRequest = query;
    myHttpConnection->setHost(ourCurrentServer, 80);
    
    if (ourProxy != "undefined") { 
        myHttpConnection->setProxy(ourProxy, ourPort);
	}

    query.prepend(ourServersSearchSchema[ourCurrentServer]);

    qDebug() << "NetworkManager::download request =" << ourCurrentServer << query;
	int id = myHttpConnection->get(query, out);
    
    return id;
}

// separate connection for covers
int NetworkManager::downloadCover(QString urlStr, QIODevice* out) {
    QUrl url(urlStr);
    myConnectionForCovers->setHost(url.host(), 80);
    if (ourProxy != "undefined") { 
        myConnectionForCovers->setProxy(ourProxy, ourPort);
	}

   // qDebug() << "NetworkManager::downloadCover request =" << url.host()<<  url.path();
	int id = myConnectionForCovers->get(url.path(), out);
    return id;
}

int NetworkManager::downloadByUrl(const QString& urlStr, QIODevice* out) {
    QString request (urlStr);
    QUrl url(urlStr);
    myHttpConnection->setHost(url.host(), 80);

    request.remove("http://");
    request.remove(url.host());
    
    qDebug() << "NetworkManager::downloadByUrl host =" << url.host()<< "request =  " << request;
	return myHttpConnection->get(request, out);
}   


void NetworkManager::showConnectionState (int /*state*/) {
    //qDebug() << "NetworkManager::connectionState " << state;
    // 0 unconnected
    // 1 host lookup
    // 2 connecting
    // 3 sending
    // 4 reading
    // 5 connected
    // 6 closing
}

void NetworkManager::initializeMap(){
    //ourServersSearchSchema.insert(SERVER_FEEDBOOKS, OPENSEARCH_FEEDBOOKS);
    ourServersSearchSchema.insert(SERVER_BOOKSERVER, OPENSEARCH_BOOKSERVER);
    //ourServersSearchSchema.insert(SERVER_ONLY_MAWHRIN, OPENSEARCH_ONLY_MAWHRIN);
    //ourServersSearchSchema.insert(SERVER_MANYBOOKS, OPENSEARCH_MANYBOOKS);
    //ourServersSearchSchema.insert(SERVER_SMASHWORDS, OPENSEARCH_SMASHWORDS);
    //ourServersSearchSchema.insert(SERVER_MUNSEYS, OPENSEARCH_MUNSEYS);
}

// return true if succeed
bool NetworkManager::setNextServer() {
    //get index of current Server
    
    //qDebug() << "NetworkManager::setNextServer";
    typedef QMap<QString, QString>::const_iterator MapIt;
    MapIt it = ourServersSearchSchema.find(ourCurrentServer);
    if ((it == ourServersSearchSchema.end()) || (++it == ourServersSearchSchema.end())) {
        ourCurrentServer = ourServersSearchSchema.begin().key();
        return false;
    }
    ourCurrentServer = it.key();
    //qDebug() << "NetworkManager::setNextServer server changed " << ourCurrentServer;
    return true;
}

void NetworkManager::getServers(QList<QString>& servers) const {
    servers += ourServersSearchSchema.keys();
    return;
}
    
/*size_t NetworkManager::getServersNumber() const {
    return ourServersSearchSchema.size(); 
}
*/	
int NetworkManager::repeatDownloading(QIODevice* out) {
    return download(myOldRequest, out);   
}
