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
    const QString& getServerUrl() const;

protected:

    void parseReceivedData(int requestId);
    void parseError(int requestId);

private:

    LinksInformation* myLinksInfo;
};


inline const QString& LinksExtractionDownloader::getServerUrl() const {
    return myServerUrl;
}

#endif // LINKSEXTRACTIONDOWNLOADER_H
