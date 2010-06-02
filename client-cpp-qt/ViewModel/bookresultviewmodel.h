#ifndef BOOKRESULTVIEWMODEL_H
#define BOOKRESULTVIEWMODEL_H

#include <QObject>
#include <QMap>
#include <QIcon>
#include <QFile>

class Book;
class BookResultsViewModel;

class BookResultViewModel : public QObject {

Q_OBJECT

public:

    BookResultViewModel(Book* book, BookResultsViewModel* parent);
    ~BookResultViewModel();

public:

    void downloadCover();

    QString getBookName();

    QString getAuthorName();

    QString getLanguage();

    QString getServerName();

    QString getFileName();

    QMap<QString, QString> getLinks();

    QIcon* getCoverIcon() const;

    bool canBeDownloaded();

    bool isDownloaded();

public slots:

    void addBookToLibraryRequested();
    void readRequested();
    void removeBookFromLibraryRequested();
    void bookInfoRequested();
    void downloadingRequested(const QString& fileName);
    void downloadingRequested();


signals:

    void bookDownloadStateChanged(QString newState);
    void infoOpenRequested(Book* book);
    void bookCoverChanged(QIcon* icon);
    void downloadStarted();
    void downloadFinished();

private slots:

    void downloadFinished(bool, int);
    void coverChanged(QIcon* icon);


private:

    void setConnections();


private:

    Book* myShownBook;
    BookResultsViewModel* myParentModel;
    QString myFileNameForSaving;
    QIcon* myCoverIcon;
    int lastRequestId;
};

#endif // BOOKRESULTVIEWMODEL_H
