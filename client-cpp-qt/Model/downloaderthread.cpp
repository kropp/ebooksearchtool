#include "downloaderthread.h"

#include "settingsmanager.h"

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
    myRequestUrl = downloadBooksRequestUrl;
    myIsFinished = true;

    myConnection = new QHttp(this);

    myConnection->setHost(myServerUrl, CONNECTION_PORT);

 //  myConnection->setProxy("192.168.0.2", 3128);

    connect(myConnection, SIGNAL(requestFinished(int, bool)), this, SLOT(requestFinished(int, bool)));
}

void DownloaderThread::parseReceivedData(int /*requestId*/)
{
    // Not Implemented
}

void DownloaderThread::parseError(int /*requestId*/)
{
    // Not Implemented
}

void DownloaderThread::requestFinished(int requestId, bool error)
{
  //  qDebug() << "DownloaderThread::requestFinished id = " << requestId;
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
    qDebug() << "DownloaderThread::download request" <<  myServerUrl << searchRequest;

    QString proxy = SettingsManager::getInstance()->getProxy();

    if (proxy.size() != 0)
    {
       myConnection->setProxy(proxy, SettingsManager::getInstance()->getProxyPort());
    }

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
    int id = download(myRequestUrl + searchRequest);
    return id;
}

bool DownloaderThread::isFinished()
{
    return myIsFinished;
}
