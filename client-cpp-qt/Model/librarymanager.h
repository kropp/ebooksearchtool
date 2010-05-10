#ifndef LIBRARYMANAGER_H
#define LIBRARYMANAGER_H

#include <QObject>
#include <QVector>

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

private:

    static LibraryManager instance;

    QVector<Book*> myBooksInLibrary;

};

#endif // LIBRARYMANAGER_H
