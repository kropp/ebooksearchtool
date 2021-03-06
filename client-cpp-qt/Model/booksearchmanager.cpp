#include "booksearchmanager.h"
#include "bookdownloader.h"
#include "book.h"

#include "servers.h"

BookSearchManager BookSearchManager::instance;

BookSearchManager::BookSearchManager()
{
    myDownloadedBooks = QVector<Book*>();
    myBookDownloaders = QVector<BookDownloader*>();

    initializeDownloaders();
    setConnectionsWithDownloaders();
    setConnections();

    downloadersSearchStarted = false;
}


BookSearchManager* BookSearchManager::getInstance()
{
    return &instance;
}

void BookSearchManager::setConnections()
{
    connect(EBookSearchTool::getInstance(), SIGNAL(serversChanged()), this, SLOT(serversChanged()));
}

void BookSearchManager::serversChanged()
{
    initializeDownloaders();
    setConnectionsWithDownloaders();
    downloadersSearchStarted = false;
}

void BookSearchManager::initializeDownloaders()
{
    myBookDownloaders.clear();

    foreach (ServerInfo* serverInfo, EBookSearchTool::getInstance()->getServers())
    {
        if (serverInfo->includedInBookSearch)
        {
            myBookDownloaders.append(new BookDownloader(serverInfo->ServerPath, serverInfo->SearchPath));
        }
    }

}

void BookSearchManager::getMore()
{
    if (downloadersSearchStarted)
    {
        for (int i = 0; i < myBookDownloaders.size(); i++)
        {
            BookDownloader* nextDownloader = myBookDownloaders.at(i);
            if (nextDownloader->isFinished())
            {
                nextDownloader->getMore();
            }
        }
    }
    else
    {
        startSearch(lastRequest);
    }
}

void BookSearchManager::setConnectionsWithDownloaders()
{
    for (int i = 0; i < myBookDownloaders.size(); i++)
    {
        BookDownloader* nextDownloader = myBookDownloaders.at(i);
        connect(nextDownloader, SIGNAL(downloadFinished(bool,QVector<Book*>*)), this, SLOT(childDownloaderFinished(bool,QVector<Book*>*)));
    }
}

void BookSearchManager::childDownloaderFinished(bool success, QVector<Book*>* gotBooks)
{
    if (success)
    {
        for (int i = 0; i < gotBooks->size(); i++)
        {
            Book* nextBook = gotBooks->at(i);
            myDownloadedBooks.append(nextBook);
        }

        emit booksChanged(myDownloadedBooks);
        emit booksAvailabilityChanged(myDownloadedBooks.size() > 0);
    }

    updateFinishedState();
}

void BookSearchManager::stop()
{
    shouldContinue = false;
    emit searchFinished();
}

void BookSearchManager::startSearch(QString searchRequest)
{
    //emit booksChanged(downloadedBooks);

    shouldContinue = true;
    lastRequest = searchRequest;
    downloadersSearchStarted = true;

    myDownloadedBooks.clear();

    for (int i = 0; i < myBookDownloaders.size(); i++)
    {
        BookDownloader* nextDownloader = myBookDownloaders.at(i);
        nextDownloader->startDownloadingBooks(searchRequest);
    }
    emit searchStarted();
}

void BookSearchManager::updateFinishedState()
{
    bool finished = true;

    for (int i = 0; i < myBookDownloaders.size(); i++)
    {
        BookDownloader* nextDownloader = myBookDownloaders.at(i);

        if (!nextDownloader->isFinished())
        {
            finished = false;
            break;
        }
    }

    if (finished)
    {
        if (shouldContinue)
        {
            getMore();
        }
        else
        {
            emit downloadFinished();
        }
    }
}
