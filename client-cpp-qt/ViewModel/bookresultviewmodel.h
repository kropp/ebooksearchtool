#ifndef BOOKRESULTVIEWMODEL_H
#define BOOKRESULTVIEWMODEL_H

#include <QObject>
class Book;
class BookResultsViewModel;

class BookResultViewModel : public QObject
{

Q_OBJECT

public:

    BookResultViewModel(Book* book, BookResultsViewModel* parent);


public:

    QString getBookName();

    QString getAuthorName();

    QString getLanguage();

    QString getServerName();

    bool canBeDownloaded();

public slots:

    void addBookToLibraryRequested();
    void removeBookFromLibraryRequested();
    void bookInfoRequested();
    void downloadingRequested();

signals:

    void infoOpenRequested(Book* book);

private slots:


private:

    void setConnections();

private:

    Book* myShownBook;
    BookResultsViewModel* myParentModel;

};

#endif // BOOKRESULTVIEWMODEL_H
