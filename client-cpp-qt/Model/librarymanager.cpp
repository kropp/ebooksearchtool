#include <QFile>
#include <QDebug>

#include "opds_parser/parser.h"
#include "opds_writer/opds_writer.h"

#include "settingsmanager.h"
#include "librarymanager.h"
#include "book.h"
#include "search_result.h"


LibraryManager LibraryManager::instance;

LibraryManager::LibraryManager()
{
    setConnections();
}

LibraryManager* LibraryManager::getInstance()
{
    return &instance;
}

void LibraryManager::setConnections()
{
    connect(SettingsManager::getInstance(), SIGNAL(libraryPathChanged()), this, SLOT(openLibrary()));
}

void LibraryManager::openLibrary() {

    myBooksInLibrary.clear();

    qDebug() << "LibraryManageri::openLibrary()";
    QString fileName = SettingsManager::getInstance()->getLibraryPath();
    if (!QFile::exists(fileName)) {
        emit booksChanged(myBooksInLibrary);
        emit booksAvailabilityChanged(false);
        return;
    }
    QFile file(SettingsManager::getInstance()->getLibraryPath());
    if (file.open(QIODevice::ReadOnly)) {
        getBooks(file);
    }
    file.close();
}

bool LibraryManager::getBooks(QFile& file) {

    OPDSParser parser;
    SearchResult searchResult;
    parser.parse(&file, &myBooksInLibrary, searchResult);

    emit booksChanged(myBooksInLibrary);
    emit booksAvailabilityChanged(myBooksInLibrary.size() > 0);

    if (myBooksInLibrary.empty()) {
        return false;
    }
    qDebug() << "set Books to library";

    return true;
}

void LibraryManager::addBookToLibrary(Book* newBook)
{
    if (!myBooksInLibrary.contains(newBook))
    {
        myBooksInLibrary.append(newBook);
        emit booksChanged(myBooksInLibrary);
        emit booksAvailabilityChanged(myBooksInLibrary.size() > 0);
        saveLibrary();
    }
}

void LibraryManager::removeBookFromLibrary(Book* newBook)
{
    if (myBooksInLibrary.contains(newBook))
    {
        myBooksInLibrary.remove(myBooksInLibrary.indexOf(newBook, 0));
        emit booksChanged(myBooksInLibrary);
        emit booksAvailabilityChanged(myBooksInLibrary.size() > 0);
        saveLibrary();
    }
}

void LibraryManager::saveLibrary() {
    const QString& fileName = SettingsManager::getInstance()->getLibraryPath();
    QFile file(fileName);
    qDebug() << "LibraryManager::saveLibrary() " << file.fileName();

    if (!file.open(QIODevice::WriteOnly)) {
        return;
    }
    DataWriter writer;
    writer.write(&file, myBooksInLibrary);
    file.close();
}

