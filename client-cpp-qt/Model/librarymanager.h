#ifndef LIBRARYMANAGER_H
#define LIBRARYMANAGER_H

#include "search_result.h"

#include <QObject>
#include <QVector>

class QFile;
class Book;

class LibraryManager : public QObject
{

    Q_OBJECT

private:

    LibraryManager();

public:

    static LibraryManager* getInstance();

signals:

    void booksChanged(QVector<Book*>& books);
    void booksAvailabilityChanged(bool availability);

public slots:

    void addBookToLibrary(Book* newBook);
    void removeBookFromLibrary(Book* newBook);
    void saveLibrary();
    void openLibrary();

private:
     bool getBooks(QFile& file);


private:

    static LibraryManager instance;

    QVector<Book*> myBooksInLibrary;
    //SearchResult mySearchResult;

};

#endif // LIBRARYMANAGER_H
