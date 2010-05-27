#include <QFile>
#include <QFileInfo>
#include <QDebug>

#include "filedownloader.h"
#include "filedownloadmanager.h"


FileDownloader::FileDownloader(QString serverName, bool openAfterDownload)
    :DownloaderThread(serverName, "")
{
    autoOpenFlag = openAfterDownload;
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

        if (autoOpenFlag) {
            QFileInfo fileInfo(filename);
            qDebug() << "FileDownload::parseReceivedData  open file" << fileInfo.absoluteFilePath();
            FileDownloadManager::openLocalFile(fileInfo.absoluteFilePath());
        }


        myResultsMutex.unlock();
    }
}

void FileDownloader::parseError(int requestId)
{
    emit downloadFinished(false, myDownloadMapping.value(requestId), requestId);
}

int FileDownloader::startDownloadingFile(QString url, QString filename, bool autoOpen)
{
    //qDebug() << "FileDownloader::startDownloadingCatalog " << myServerUrl << searchRequest;
    autoOpenFlag = autoOpen;
    int requestId = startDownloading(url);
    myDownloadMapping.insert(requestId, filename);
    return requestId;
}

