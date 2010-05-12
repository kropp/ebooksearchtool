#include <QFile>
#include <QDebug>
#include <QUrl>


#include "opds_parser/parser.h"
#include "linksextractiondownloader.h"

static const int CONNECTION_PORT = 80;
static const int REDIRECT_CODE = 301;

static const QString LOCATION_HEADER_TITLE = "Location";

static const QString HTTP_PREFIX = "http://";

LinksExtractionDownloader::LinksExtractionDownloader(QString downloadServerUrl, QString downloadBooksRequestUrl)
    :DownloaderThread(downloadServerUrl, downloadBooksRequestUrl)
{

}

void LinksExtractionDownloader::startExtractingLinks()
{
    qDebug() << "LinksExtractionDownloader:: startExtractingLinks " <<  myBooksRequestUrl;
    startDownloading(myBooksRequestUrl);
}


void LinksExtractionDownloader::parseReceivedData(int /*requestId*/)
{

    qDebug() << "LinksExtractionDownloader::parseRecievedData ";
   /* if (myInputBuffer)
    {
        myResultsMutex.lock();

        myInputBuffer->open(QIODevice::ReadOnly);

        myDownloadedBooks = new QVector<Book*>();

        OPDSParser parser;
        if (!parser.parse(myInputBuffer, myDownloadedBooks, mySearchResult)) {
            myDownloadedBooks->clear();
        }

        for (int i = 0; i < myDownloadedBooks->size(); i++)
        {
            Book* nextBook = myDownloadedBooks->at(i);
            nextBook->setServerName(myServerUrl);
        }

        myResultsMutex.unlock();

                 myIsFinished = true;

        //}

        emit downloadFinished(true, myDownloadedBooks);
    }
*/
}


void LinksExtractionDownloader::parseError(int /*requestId*/)
{
    // TODO decrease counter
    emit downloadFinished(false, 0);
}

