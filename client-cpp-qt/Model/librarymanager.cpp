#include <QFile>
#include <QDebug>

#include "opds_parser/parser.h"
#include "opds_writer/opds_writer.h"

#include "librarymanager.h"
#include "book.h"
#include "settings.h"
#include "search_result.h"


LibraryManager LibraryManager::instance;

LibraryManager::LibraryManager()
{
}

LibraryManager* LibraryManager::getInstance()
{
    return &instance;
}

void LibraryManager::openLibrary() {
    qDebug() << "LibraryManageri::openLibrary()";
    QString fileName = Settings::getInstance().getLibraryPath();
    if (!QFile::exists(fileName)) {
        return;
    }
    QFile file(Settings::getInstance().getLibraryPath());
    if (file.open(QIODevice::ReadOnly)) {
        getBooks(file);
    }
    file.close();
}

bool LibraryManager::getBooks(QFile& file) {
    OPDSParser parser;
    SearchResult searchResult;
    parser.parse(&file, &myBooksInLibrary, searchResult);
    if (myBooksInLibrary.empty()) {
        return false;
    }
    qDebug() << "set Books to library";
    emit booksChanged(myBooksInLibrary);
    emit booksAvailabilityChanged(myBooksInLibrary.size() > 0);
    return true;
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

void LibraryManager::saveLibrary() {
    const QString& fileName = Settings::getInstance().getLibraryPath();
    QFile file(fileName);
    qDebug() << "LibraryManager::saveLibrary() " << file.fileName();

    if (!file.open(QIODevice::WriteOnly)) {
        return;
    }
    DataWriter writer;
    writer.write(&file, myBooksInLibrary);
    file.close();
}

