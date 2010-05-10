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

private slots:

    void childDownloaderFinished(bool success, QVector<Book*>* gotBooks);

private:

    void initializeDownloaders();
    void recreateDownloaders();
    void abortDownloaders();
    void setConnectionsWithDownloaders();
    void disconnectDownloaders();
    void updateFinishedState();

private:

    static BookSearchManager instance;

    QVector<Book*> myDownloadedBooks;

    QVector<BookDownloader*> myBookDownloaders;
};

#endif // NETWORKMANAGER_H
