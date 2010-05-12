#ifndef LINKSEXTRACTIONDOWNLOADER_H
#define LINKSEXTRACTIONDOWNLOADER_H

#include <QObject>
#include <QVector>

#include <QIODevice>
#include <QHttp>
#include <QBuffer>
#include <QMutex>

#include "downloaderthread.h"
#include "linksinformation.h"

class LinksExtractionDownloader : public DownloaderThread
{

    Q_OBJECT

public:

    LinksExtractionDownloader(QString downloadServerUrl, QString downloadBooksRequestUrl);

signals:

    void downloadFinished(bool success, LinksInformation* linksInfo);

public:

    void startExtractingLinks();

protected:

    void parseReceivedData(int requestId);
    void parseError(int requestId);

private:

    LinksInformation* myLinksInfo;
};

#endif // LINKSEXTRACTIONDOWNLOADER_H
