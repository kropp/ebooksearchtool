#ifndef NETWORKMANAGER_H
#define NETWORKMANAGER_H

#include <QObject>
#include <QVector>

class Book;
class BookDownloader;

class BookSearchManager : public QObject
{

Q_OBJECT

private:

    BookSearchManager();

public:

    static BookSearchManager* getInstance();

signals:

    void booksChanged(QVector<Book*>& books);
    void booksAvailabilityChanged(bool availability);
    void downloadFinished();

public slots:

    void startSearch(QString searchRequest);
    void getMore();
    void stop();

private slots:

    void childDownloaderFinished(bool success, QVector<Book*>* gotBooks);
    void serversChanged();

private:

    void initializeDownloaders();
    void recreateDownloaders();
    void abortDownloaders();
    void setConnectionsWithDownloaders();
    void disconnectDownloaders();
    void updateFinishedState();
    void setConnections();


private:

    static BookSearchManager instance;

    bool downloadersSearchStarted;

    QVector<Book*> myDownloadedBooks;

    QVector<BookDownloader*> myBookDownloaders;

    QString lastRequest;

    bool shouldContinue;
};

#endif // NETWORKMANAGER_H
