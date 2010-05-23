#ifndef BOOKRESULTVIEWMODEL_H
#define BOOKRESULTVIEWMODEL_H

#include <QObject>
#include <QMap>
#include <QIcon>
#include <QFile>

class Book;
class BookResultsViewModel;

class BookResultViewModel : public QObject
{

Q_OBJECT

public:

    BookResultViewModel(Book* book, BookResultsViewModel* parent);


public:

    void downloadCover();

    QString getBookName();

    QString getAuthorName();

    QString getLanguage();

    QString getServerName();

    QString getFileName();

    QMap<QString, QString> getLinks();

    bool canBeDownloaded();

public slots:

    void addBookToLibraryRequested();
    void readRequested();
    void removeBookFromLibraryRequested();
    void bookInfoRequested();
    void downloadingRequested(const QString& fileName);

signals:

    void bookDownloadStateChanged(QString newState);
    void infoOpenRequested(Book* book);
    void bookCoverChanged(QIcon* icon);

private slots:

    void downloadFinished(bool, int);

private:

    void setConnections();


private:

    Book* myShownBook;
    BookResultsViewModel* myParentModel;

    int lastRequestId;
};

#endif // BOOKRESULTVIEWMODEL_H
