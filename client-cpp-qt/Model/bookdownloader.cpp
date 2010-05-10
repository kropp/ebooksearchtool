#include "bookdownloader.h"
#include "book.h"
#include "opds_parser/parser.h"
#include "search_result.h"

#include <QFile>
#include <QDebug>
#include <QUrl>

static const int CONNECTION_PORT = 80;
static const int REDIRECT_CODE = 301;

static const QString LOCATION_HEADER_TITLE = "Location";

static const QString HTTP_PREFIX = "http://";

BookDownloader::BookDownloader(QString downloadServerUrl, QString downloadBooksRequestUrl)
    :DownloaderThread(downloadServerUrl, downloadBooksRequestUrl)
{

}

void BookDownloader::startDownloadingBooks(QString searchRequest)
{
    startDownloading(searchRequest);
}


void BookDownloader::parseReceivedData(int requestId)
{
    if (myInputBuffer)
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

       /* if (searchResult.hasNextResult())
        {
            QList<QString>* nextResults = searchResult.getLinks();

           // for (int i = 0; i < nextResults.size(); i++)
           // {
                QString nextResult = nextResults->at(0);
                int serverNameIndex = nextResult.indexOf(HTTP_PREFIX + serverUrl);

                if (serverNameIndex == 0)
                {
                    nextResult.remove(0, (HTTP_PREFIX + serverUrl).size());
                }

                searchResult.clearResults();
                download(nextResult);
            //}
        }
        else
        {*/
            myIsFinished = true;

        //}

        emit downloadFinished(true, myDownloadedBooks);
    }
}

void BookDownloader::getMore()
{
    if (mySearchResult.hasNextResult())
    {
        QList<QString>* nextResults = mySearchResult.getLinks();

        QString nextResult = nextResults->at(0);
        int serverNameIndex = nextResult.indexOf(HTTP_PREFIX + myServerUrl);

        if (serverNameIndex == 0)
        {
            nextResult.remove(0, (HTTP_PREFIX + myServerUrl).size());
        }

        mySearchResult.clearResults();
        download(nextResult);
    }
}

void BookDownloader::parseError(int requestId)
{
    emit downloadFinished(false, new QVector<Book*>());
}
