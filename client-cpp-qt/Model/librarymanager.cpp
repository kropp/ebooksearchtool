#include "librarymanager.h"
#include "book.h"

LibraryManager LibraryManager::instance;

LibraryManager::LibraryManager()
{
    myBooksInLibrary = QVector<Book*>();
}

LibraryManager* LibraryManager::getInstance()
{
    return &instance;
}

void LibraryManager::addBookToLibrary(Book* newBook)
{
    if (!myBooksInLibrary.contains(newBook))
    {
        myBooksInLibrary.append(newBook);
        emit booksChanged(myBooksInLibrary);
        emit booksAvailabilityChanged(myBooksInLibrary.size() > 0);
    }
}

void LibraryManager::removeBookFromLibrary(Book* newBook)
{
    if (myBooksInLibrary.contains(newBook))
    {
        myBooksInLibrary.remove(myBooksInLibrary.indexOf(newBook, 0));
        emit booksChanged(myBooksInLibrary);
        emit booksAvailabilityChanged(myBooksInLibrary.size() > 0);
    }
}
