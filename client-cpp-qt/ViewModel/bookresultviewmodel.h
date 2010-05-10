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

public slots:

    void addBookToLibraryRequested();
    void removeBookFromLibraryRequested();
    void bookInfoRequested();

signals:

    void infoOpenRequested(Book* book);

private slots:


private:

    void setConnections();

private:

    Book* shownBook;
    BookResultsViewModel* parentModel;

};

#endif // BOOKRESULTVIEWMODEL_H
