#include "booksearchmanager.h"
#include "bookdownloader.h"
#include "book.h"

#include "servers.h"

BookSearchManager BookSearchManager::instance;

BookSearchManager::BookSearchManager()
{
    EBookSearchTool::initializeServers();
    myDownloadedBooks = QVector<Book*>();
    myBookDownloaders = QVector<BookDownloader*>();

    initializeDownloaders();
    setConnectionsWithDownloaders();
}


BookSearchManager* BookSearchManager::getInstance()
{
    return &instance;
}

void BookSearchManager::initializeDownloaders()
{
    myBookDownloaders.clear();

    myBookDownloaders.append(new BookDownloader(FEEDBOOKS_ID, "/books/search.atom?query="));
    myBookDownloaders.append(new BookDownloader(MANYBOOKS_ID, "/stanza/search.php?q="));
    myBookDownloaders.append(new BookDownloader(BOOKSERVER_ID, "/catalog/opensearch?q="));
    myBookDownloaders.append(new BookDownloader(SMASHWORDS_ID, "/atom/search/books?query="));
    myBookDownloaders.append(new BookDownloader(EBOOKSEARCH_ID, "/search?query="));

  //  myBookDownloaders.append(new BookDownloader(LITRES_ID, "/munseys/op/search?search="));
}

void BookSearchManager::getMore()
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

void BookSearchManager::startSearch(QString searchRequest)
{
    //emit booksChanged(downloadedBooks);

    myDownloadedBooks.clear();

    for (int i = 0; i < myBookDownloaders.size(); i++)
    {
        BookDownloader* nextDownloader = myBookDownloaders.at(i);
        nextDownloader->startDownloadingBooks(searchRequest);
    }
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
        emit downloadFinished();
    }
}
