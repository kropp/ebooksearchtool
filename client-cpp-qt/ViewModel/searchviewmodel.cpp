#include "searchviewmodel.h"
#include "bookresultsviewmodel.h"

#include "../Model/book.h"
#include "../Model/booksearchmanager.h"


SearchViewModel::SearchViewModel()
{
    resultsAvailability = false;

    bookResultsVm = new BookResultsViewModel();
    bookResultsVm->initialize();
    myBookInfoVm = new InformationViewModel();

    setConnections();
}

BookResultsViewModel* SearchViewModel::getBookResultsViewModel()
{
    return bookResultsVm;
}

void SearchViewModel::searchStartRequested(QString searchRequest)
{
    //setAvailability(!resultsAvailability);
    BookSearchManager::getInstance()->startSearch(searchRequest);
}

void SearchViewModel::stopRequested()
{
    BookSearchManager::getInstance()->stop();
}

void SearchViewModel::setSearchResultsAvailability(bool availability)
{
    if (resultsAvailability != availability)
    {
        resultsAvailability = availability;
        emit resultsAvailabilityChanged(availability);
    }
}

void SearchViewModel::bookResultsVisibilityChanged(bool visibility)
{
    setSearchResultsAvailability(visibility);
}

void SearchViewModel::setConnections()
{
    connect(BookSearchManager::getInstance(), SIGNAL(booksAvailabilityChanged(bool)), this, SLOT(bookResultsVisibilityChanged(bool)));
    connect(bookResultsVm, SIGNAL(infoOpenRequested(Book*)), this, SIGNAL(infoOpenRequested(Book*)));
    connect(bookResultsVm, SIGNAL(infoOpenRequested(Book*)), myBookInfoVm, SLOT(changeCurrentBook(Book*)));


//    connect(bookResultsVm, SIGNAL(shownBooksChanged(QVector<BookResultViewModel*>)), myBookInfoVm, SLOT(forgetInfo()));

    // connect(bookResultsVm, SIGNAL(pageChanged(int)), myBookInfoVm, SLOT(forgetInfo()));
    //void shownBooksChanged(QVector<BookResultViewModel*> newBooks);

    //connect(bookResultsVm, SIGNAL(infoOpenRequested(Book*)), myBookInfoVm, SLOT(changeCurrentBook(Book*)));
}
