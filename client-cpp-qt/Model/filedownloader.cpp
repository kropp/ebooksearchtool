#include <QFile>
#include <QDebug>

#include "filedownloader.h"

FileDownloader::FileDownloader(QString serverName)
    :DownloaderThread(serverName, "")
{

}


void FileDownloader::parseReceivedData(int requestId)
{
    qDebug() << "FileDownloader::parseReceivedData() ";
    if (myInputBuffer)
    {
        myResultsMutex.lock();

        myInputBuffer->open(QIODevice::ReadOnly);
        QString filename = myDownloadMapping.value(requestId);
        QFile file(filename);
        if (!file.open(QIODevice::WriteOnly)) {
            qDebug() << "FileDownloader error - open file " << filename;

            emit downloadFinished(false, filename, requestId);
            return;
        }
        file.write(myInputBuffer->buffer());
        file.close();

        qDebug() << "FileDownloader file downloaded " << filename;
        myIsFinished = true;
        emit downloadFinished(true, filename, requestId);

        myResultsMutex.unlock();
    }
}

void FileDownloader::parseError(int requestId)
{
    emit downloadFinished(false, myDownloadMapping.value(requestId), requestId);
}

int FileDownloader::startDownloadingFile(QString url, QString filename)
{
    //qDebug() << "FileDownloader::startDownloadingCatalog " << myServerUrl << searchRequest;

    int requestId = startDownloading(url);
    myDownloadMapping.insert(requestId, filename);
    return requestId;
}

