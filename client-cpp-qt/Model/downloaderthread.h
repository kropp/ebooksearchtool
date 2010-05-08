#ifndef DOWNLOADERTHREAD_H
#define DOWNLOADERTHREAD_H

#include <QObject>
#include <QVector>

#include <QIODevice>
#include <QHttp>
#include <QBuffer>
#include <QMutex>

#include "search_result.h"

class DownloaderThread : public QObject
{

    Q_OBJECT

public:

    DownloaderThread(QString downloadServerUrl, QString downloadBooksRequestUrl);

public:

    void abort();
    bool isFinished();

private slots:

    void requestFinished(int requestId, bool error);

protected:

    int startDownloading(QString searchRequest);
    virtual void parseReceivedData(int requestId);
    virtual void parseError(int requestId);

protected:

    int download(QString searchRequest);
    void finishDownloading();

protected:

    SearchResult mySearchResult;
    QString myServerUrl;
    QString myBooksRequestUrl;

    bool myIsFinished;

    QMutex myResultsMutex;

    QHttp* myConnection;
    QBuffer* myInputBuffer;

    int myCurrentRequestId;

    QString redirectLink;
};

#endif // DOWNLOADERTHREAD_H
