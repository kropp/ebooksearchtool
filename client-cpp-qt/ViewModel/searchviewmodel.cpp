#include "searchviewmodel.h"
#include "bookresultsviewmodel.h"

#include "../Model/book.h"
#include "../Model/booksearchmanager.h"


SearchViewModel::SearchViewModel()
{
    myResultsAvailability = false;

    myBookResultsVm = new BookResultsViewModel();
    myBookResultsVm->initialize();

    setConnections();
}

BookResultsViewModel* SearchViewModel::getBookResultsViewModel()
{
    return myBookResultsVm;
}

void SearchViewModel::searchStartRequested(QString searchRequest)
{
    //setAvailability(!resultsAvailability);
    BookSearchManager::getInstance()->startSearch(searchRequest);
    //NetworkManager::getInstance()->startSearch(searchRequest);
}

void SearchViewModel::moreBooksRequested()
{
    BookSearchManager::getInstance()->getMore();
}

void SearchViewModel::setSearchResultsAvailability(bool availability)
{
    if (myResultsAvailability != availability)
    {
        myResultsAvailability = availability;
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
    connect(myBookResultsVm, SIGNAL(infoOpenRequested(Book*)), this, SIGNAL(infoOpenRequested(Book*)));
}
