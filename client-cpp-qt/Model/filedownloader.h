#ifndef FILEDOWNLOADER_H
#define FILEDOWNLOADER_H


#include <QObject>
#include <QVector>

#include <QIODevice>
#include <QHttp>
#include <QBuffer>
#include <QMutex>

#include "downloaderthread.h"

class FileDownloader : public DownloaderThread
{

    Q_OBJECT

public:

    FileDownloader(QString serverName);

public:

    int startDownloadingFile(QString url, QString filename);

signals:

    void downloadFinished(bool success, QString filename, int requestId);

protected:

    void parseReceivedData(int requestId);
    void parseError(int requestId);

private:

    // requestId -> file to write
    QMap<int, QString> myDownloadMapping;
};


#endif // FILEDOWNLOADER_H
