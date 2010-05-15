//#include <QFile>
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
//    qDebug() << "LinksExtractionDownloader:: startExtractingLinks "
//            <<  "server =" << myServerUrl
//            << "url = " << myRequestUrl;

    startDownloading(QString());
}


void LinksExtractionDownloader::parseReceivedData(int requestId)
{
    qDebug() << "LinksExtractionDownloader::parseRecievedData " << myRequestUrl;
    if (myInputBuffer)
    {
        myInputBuffer->open(QIODevice::ReadOnly);

        OPDSParser parser;
        parser.parseOpdsLinks(myInputBuffer, &myLinksInfo);

        myIsFinished = true;

    }

//    qDebug() << "LinksExtractionDownloader::parseReceivedData parsed emit 'download finished'" << myLinksInfo.getNewLinks()
//            << myLinksInfo.getPolularLinks();

    emit downloadFinished(true, &myLinksInfo);

   }


void LinksExtractionDownloader::parseError(int /*requestId*/)
{
    // TODO decrease counter of left links for catalog to be ready
    emit downloadFinished(false, 0);
}

