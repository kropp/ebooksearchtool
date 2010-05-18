#include <QDebug>

#include "bookresultviewmodel.h"
#include "bookresultsviewmodel.h"
#include "../Model/settings.h"

#include "../Model/book.h"
#include "../Model/librarymanager.h"
#include "../Model/filedownloadmanager.h"

#include "../Model/filedownloadmanager.h"

BookResultViewModel::BookResultViewModel(Book* book, BookResultsViewModel* parent)
{
    myShownBook = book;
    myParentModel = parent;

    setConnections();
}

void BookResultViewModel::setConnections()
{
    connect(FileDownloadManager::getInstance(), SIGNAL(downloadBookFinished(bool, int)), this, SLOT(downloadFinished(bool, int)));
}

void BookResultViewModel::downloadFinished(bool success, int requestId)
{
    if (lastRequestId == requestId)
    {
        if (success)
        {
            emit bookDownloadStateChanged("downloaded");
        }
        else
        {
            emit bookDownloadStateChanged("failed");
        }
    }
}


QMap<QString, QString> BookResultViewModel::getLinks() {
    return myShownBook->getSourceLinks();
}


QString BookResultViewModel::getBookName()
{
    return myShownBook->getTitle().trimmed();
}

QString BookResultViewModel::getServerName()
{
    return myShownBook->getServerName();
}

QString BookResultViewModel::getAuthorName()
{
    if (myShownBook->getAuthors().size() > 0)
    {
        return myShownBook->getAuthors().at(0)->getName().trimmed();
    }
    else
    {
        if (myShownBook->getAuthors().size() == 0)
        {
            return "Unknown author";
        }
        else
        {
            return "Various authors";
        }
    }
}

QString BookResultViewModel::getLanguage()
{
    return myShownBook->getLanguage();
}

QString BookResultViewModel::getFileName() {
    QString name = myShownBook->getTitle();
    name.replace(" ", "_");
    return name;
}

void BookResultViewModel::addBookToLibraryRequested()
{
    LibraryManager::getInstance()->addBookToLibrary(myShownBook);
}

void BookResultViewModel::removeBookFromLibraryRequested()
{
    LibraryManager::getInstance()->removeBookFromLibrary(myShownBook);
}

void BookResultViewModel::bookInfoRequested()
{
    myParentModel->bookInfoRequested(myShownBook);
}

void BookResultViewModel::downloadingRequested(const QString& filename) {

    qDebug() << "BookResultViewModel::downloadingRequested() link " << myShownBook->getSourceLinks()
             << "file" << filename;

    lastRequestId = FileDownloadManager::getInstance()->downloadBook(*myShownBook, filename, Settings::FORMAT);
}


bool BookResultViewModel::canBeDownloaded() {
    //qDebug() << "BookResultViewModel::canBeDownloaded() " << myShownBook->getSourceLinks();

    return !myShownBook->getSourceLinks().value(Settings::FORMAT).isEmpty();
}
