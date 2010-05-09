#include "downloaderthread.h"

#include <QFile>
#include <QDebug>
#include <QUrl>

static const int CONNECTION_PORT = 80;
static const int REDIRECT_CODE = 301;

static const QString LOCATION_HEADER_TITLE = "Location";

DownloaderThread::DownloaderThread(QString downloadServerUrl, QString downloadBooksRequestUrl)
{
    myInputBuffer = 0;
    myServerUrl = downloadServerUrl;
    myBooksRequestUrl = downloadBooksRequestUrl;
    myIsFinished = true;

    myConnection = new QHttp(this);

    myConnection->setHost(myServerUrl, CONNECTION_PORT);

    connect(myConnection, SIGNAL(requestFinished(int, bool)), this, SLOT(requestFinished(int, bool)));
}

void DownloaderThread::requestFinished(int requestId, bool error)
{
    if (requestId == myCurrentRequestId)
    {
        if (!error)
        {
            if (myInputBuffer)
            {
                parseReceivedData(requestId);
            }
        }
        else
        {
            myIsFinished = true;
            parseError(requestId);
        }
    }
}

int DownloaderThread::download(QString searchRequest)
{
    myResultsMutex.lock();

    myIsFinished = false;

    myInputBuffer = new QBuffer(this);

    myResultsMutex.unlock();

    return myCurrentRequestId = myConnection->get(searchRequest, myInputBuffer);
}

void DownloaderThread::abort()
{
    myResultsMutex.lock();

    myConnection->abort();

    myResultsMutex.unlock();
}

int DownloaderThread::startDownloading(QString searchRequest)
{
    return download(myBooksRequestUrl + searchRequest);
}

bool DownloaderThread::isFinished()
{
    return myIsFinished;
}

void DownloaderThread::parseError(int /*requestId*/)
{

}

void DownloaderThread::parseReceivedData(int /*requestId*/)
{

}
