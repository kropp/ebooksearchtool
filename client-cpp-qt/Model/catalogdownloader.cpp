#include "catalogdownloader.h"

#include "bookdownloader.h"
#include "book.h"
#include "catalog.h"
#include "opds_parser/parser.h"

#include <QFile>
#include <QDebug>
#include <QUrl>

static const int CONNECTION_PORT = 80;
static const int REDIRECT_CODE = 301;

static const QString LOCATION_HEADER_TITLE = "Location";

CatalogDownloader::CatalogDownloader(QString serverName)
    :DownloaderThread(serverName, "")
{
    myDownloadMapping = new QMap<QString, Catalog*>();

}


void CatalogDownloader::parseReceivedData(int requestId)
{
    if (myInputBuffer)
    {
        myResultsMutex.lock();

        myInputBuffer->open(QIODevice::ReadOnly);

        QVector<Book*>* downloadedBooks = new QVector<Book*>();
        QVector<Catalog*>* downloadedCatalogs = new QVector<Catalog*>();

        Catalog* requestCatalog = myDownloadMapping->value(QString::number(requestId));

        OPDSParser parser;
        if (!parser.parseBooksOrCatalogs(myInputBuffer, downloadedBooks, downloadedCatalogs, mySearchResult, myServerUrl)) {
            downloadedBooks->clear();
            downloadedCatalogs->clear();
        }

        if (downloadedBooks->size() != 0)
        {
            requestCatalog->setCatalogType(true);

            for (int i = 0; i < downloadedBooks->size(); i++)
            {
                Book* nextBook = downloadedBooks->at(i);
                nextBook->setServerName(myServerUrl);

                requestCatalog->addBookToCatalog(nextBook);
            }
        }
        else
        {
            requestCatalog->setCatalogType(false);

            for (int i = 0; i < downloadedCatalogs->size(); i++)
            {
                Catalog* nextCatalog = downloadedCatalogs->at(i);

                requestCatalog->addCatalogToCatalog(nextCatalog);
            }
        }

        requestCatalog->markAsParsed();

        myIsFinished = true;
        emit downloadFinished(true, requestCatalog);

        myResultsMutex.unlock();
    }
}

void CatalogDownloader::parseError(int requestId)
{
    emit downloadFinished(false, 0);
}

void CatalogDownloader::startDownloadingCatalog(QString searchRequest, Catalog* parseCatalog)
{
    qDebug() << "CatalogDownloader::startDownloadingCatalog " << myServerUrl << searchRequest;
    myDownloadMapping->insert(QString::number(startDownloading(searchRequest)), parseCatalog);
}
