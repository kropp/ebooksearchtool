#ifndef BOOKDOWNLOADER_H
#define BOOKDOWNLOADER_H

class Book;

#include <QObject>
#include <QVector>

#include <QIODevice>
#include <QHttp>
#include <QBuffer>
#include <QMutex>

#include "downloaderthread.h"
#include "search_result.h"

class BookDownloader : public DownloaderThread
{

    Q_OBJECT

public:

    BookDownloader(QString downloadServerUrl, QString downloadBooksRequestUrl);
    virtual ~BookDownloader();

signals:

    void downloadFinished(bool success, QVector<Book*>* gotBooks);

public:

    void startDownloadingBooks(QString searchRequest);
    void getMore();

protected:

    virtual void parseReceivedData(int requestId);
    virtual void parseError(int requestId);

private:

    QVector<Book*>* myDownloadedBooks;
};

#endif // BOOKDOWNLOADER_H
