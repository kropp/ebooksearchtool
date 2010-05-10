#ifndef CATALOGDOWNLOADER_H
#define CATALOGDOWNLOADER_H

class Catalog;
class Book;
class SearchResult;

#include <QObject>
#include <QVector>

#include <QIODevice>
#include <QHttp>
#include <QBuffer>
#include <QMutex>

#include "downloaderthread.h"
#include "search_result.h"

class CatalogDownloader : public DownloaderThread
{

    Q_OBJECT

public:

    CatalogDownloader(QString serverName);

public:

    void startDownloadingCatalog(QString searchRequest, Catalog* parseCatalog);

signals:

    void downloadFinished(bool success, Catalog* downloadedCatalog);

protected:

    void parseReceivedData(int requestId);
    void parseError(int requestId);

private:

    Catalog* myCurrentCatalog;
    QMap<QString, Catalog*>* myDownloadMapping;
};

#endif // CATALOGDOWNLOADER_H
