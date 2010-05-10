#ifndef BOOKDOWNLOADER_H
#define BOOKDOWNLOADER_H

class Book;
class SearchResult;

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

signals:

    void downloadFinished(bool success, QVector<Book*>* gotBooks);

public:

    void startDownloadingBooks(QString searchRequest);
    void getMore();

protected:

    void parseReceivedData(int requestId);
    void parseError(int requestId);

private:

    QVector<Book*>* myDownloadedBooks;
};

#endif // BOOKDOWNLOADER_H
