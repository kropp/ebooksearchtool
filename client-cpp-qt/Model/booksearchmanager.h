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

    /**
     * Emitted when new books are added
     */
    void booksChanged(QVector<Book*>& books);

    /**
     * Emitted with availability=true when at least one book is added after download was requested
     */
    void booksAvailabilityChanged(bool availability);

    /**
     * Emitted when all threads have finished downloading
     */
    void downloadFinished();

public slots:

    void startSearch(QString searchRequest);
    void getMore();

private slots:

    void childDownloaderFinished(bool success, QVector<Book*>* gotBooks);

private:

    void initializeDownloaders();

    /**
     * TODO
     */
    void abortDownloaders();

    void setConnectionsWithDownloaders();

    void updateFinishedState();

private:

    static BookSearchManager instance;

    QVector<Book*> myDownloadedBooks;
    QVector<BookDownloader*> myBookDownloaders;
};

#endif // NETWORKMANAGER_H
