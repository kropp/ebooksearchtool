#include "bookresultsviewmodel.h"
#include "bookresultviewmodel.h"

#include "../Model/book.h"
#include "../Model/booksearchmanager.h"

#include <QDebug>

static const int RESULTS_ON_PAGE = 15;
static const int PAGES_WINDOW_SIZE = 17;

bool sortingLessThanByAuthor(BookResultViewModel* b1, BookResultViewModel* b2)
{
    return b1->getAuthorName().toLower() < b2->getAuthorName().toLower();
}

bool sortingLessThanByLanguage(BookResultViewModel* b1, BookResultViewModel* b2)
{
    return b1->getLanguage().toLower() < b2->getLanguage().toLower();
}

bool sortingLessThanByServer(BookResultViewModel* b1, BookResultViewModel* b2)
{
    return b1->getServerName().toLower() < b2->getServerName().toLower();
}

BookResultsViewModel::BookResultsViewModel()
{
    myReceivedBooks = QVector<BookResultViewModel*>();
    myCurrentlyFilteredBooks = QVector<BookResultViewModel*>();

    myCurrentPageWithPageWindowCorrection = 0;
    myCurrentPageWithoutPageWindowCorrection = 0;
    myPageWindowIndex = 0;

    recalculatePages();
}

void BookResultsViewModel::initialize()
{
    setConnections();
}

void BookResultsViewModel::requestNextPagesWindow()
{
    myPageWindowIndex++;
    recalculatePages();
}

void BookResultsViewModel::requestPrevPagesWindow()
{
    myPageWindowIndex--;
    recalculatePages();
}

void BookResultsViewModel::recalculatePages()
{
    int totalCount = myCurrentlyFilteredBooks.size();
    bool additionalPageNeeded = (totalCount % RESULTS_ON_PAGE);
    int fullPagesCount = (totalCount / RESULTS_ON_PAGE) + (additionalPageNeeded ? 1 : 0);
    int pagesCountAfterWindowPosition = fullPagesCount - myPageWindowIndex * PAGES_WINDOW_SIZE;

    if (pagesCountAfterWindowPosition < 0)
    {
        myPageWindowIndex = 0;
        pagesCountAfterWindowPosition = (totalCount / RESULTS_ON_PAGE) + (additionalPageNeeded ? 1 : 0);
    }


    int pagesToBeShown = pagesCountAfterWindowPosition > PAGES_WINDOW_SIZE
                                        ? PAGES_WINDOW_SIZE
                                        : pagesCountAfterWindowPosition;

    if (myCurrentPageWithoutPageWindowCorrection >= pagesToBeShown)
    {
        requestToChangePage(0);
    }
    else
    {
        requestToChangePage(myCurrentPageWithoutPageWindowCorrection);
        showCurrentPage();
    }


    emit pagesCountChanged(pagesToBeShown, myPageWindowIndex * PAGES_WINDOW_SIZE);
    emit pagePrevAvailabilityChanged(myPageWindowIndex > 0);
    emit pageNextAvailabilityChanged(pagesCountAfterWindowPosition > PAGES_WINDOW_SIZE);
}

void BookResultsViewModel::requestToChangePage(int page)
{

    myCurrentPageWithoutPageWindowCorrection = page;
    myCurrentPageWithPageWindowCorrection = page + myPageWindowIndex * PAGES_WINDOW_SIZE;

    emit pageChanged(page);
    showCurrentPage();
}

void BookResultsViewModel::showCurrentPage()
{
    QVector<BookResultViewModel*> currentPageBooks;

    for (int i = myCurrentPageWithPageWindowCorrection * RESULTS_ON_PAGE; i < (myCurrentPageWithPageWindowCorrection + 1) * RESULTS_ON_PAGE; i++)
    {
        if (i < myCurrentlyFilteredBooks.size())
        {
            currentPageBooks.append(myCurrentlyFilteredBooks.at(i));
        }
    }

    emit shownBooksChanged(currentPageBooks);
}

void BookResultsViewModel::changeAllFilteringDataSimultaneously
(
    SelectionType newGroupType,
    SelectionType newSortType,
    SelectionType newFilterType,
    QString newFilterTerm
)
{
    myGroupType = newGroupType;
    mySortType = newSortType;
    myFilterType = newFilterType;
    myFilterWords = newFilterTerm;
    updateShownBooks();
}

void BookResultsViewModel::changeGroupingType(SelectionType newType)
{
    myGroupType = newType;
    updateShownBooks();
}

void BookResultsViewModel::changeSortingType(SelectionType newType)
{
    mySortType = newType;
    updateShownBooks();
}

void BookResultsViewModel::changeFilteringType(SelectionType newType)
{
    myFilterType = newType;
    updateShownBooks();
}

void BookResultsViewModel::changeFilteringPhrase(QString filter)
{
    myFilterWords = filter;
    updateShownBooks();
}

QString BookResultsViewModel::getTerm(BookResultViewModel* element, SelectionType selection)
{
    QString term;

    switch (selection)
    {
    case AUTHOR:
        term = element->getAuthorName();
        break;
    case LANGUAGE:
        term = element->getLanguage();
        break;
    case SERVER:
        term = element->getServerName();
        break;
    }

    return term;
}

void BookResultsViewModel::updateShownBooks()
{
    QVector<BookResultViewModel*> processedBooks = QVector<BookResultViewModel*>(myReceivedBooks);
    QVector<BookResultViewModel*> filteredBooks;
    
    if (myFilterType != NONE)
    {
        for (int i = 0; i < processedBooks .size(); i++)
        {
            BookResultViewModel* nextElement = processedBooks.at(i);
            QString filteredTerm = getTerm(nextElement, myFilterType);

            if (filteredTerm.contains(myFilterWords))
            {
                filteredBooks.append(nextElement);
            }
        }
    }
    else
    {
        filteredBooks = processedBooks;
    }

    if (mySortType != NONE)
    {
        switch (mySortType)
        {
        case AUTHOR:
            qSort(filteredBooks.begin(), filteredBooks.end(), sortingLessThanByAuthor);
            break;
        case LANGUAGE:
            qSort(filteredBooks.begin(), filteredBooks.end(), sortingLessThanByLanguage);
            break;
        case SERVER:
            qSort(filteredBooks.begin(), filteredBooks.end(), sortingLessThanByServer);
            break;
        }
    }

    QVector<BookResultViewModel*> groupedBooks;

    if (myGroupType != NONE)
    {
        while(filteredBooks.size() > 0)
        {
            BookResultViewModel* firstElement = filteredBooks.at(0);
            groupedBooks.append(firstElement);

            QString groupTermFirst = getTerm(firstElement, myGroupType);

            for (int i = 1; i < filteredBooks .size(); i++)
            {
                BookResultViewModel* comparedElement = filteredBooks.at(i);

                QString groupTermCompared = getTerm(comparedElement, myGroupType);

                if (!groupTermCompared.compare(groupTermFirst))
                {
                    groupedBooks.append(comparedElement);
                    filteredBooks.remove(i);
                    i--;
                }
            }

            filteredBooks.remove(0);
        }
    }
    else
    {
        groupedBooks = filteredBooks;
    }

    myCurrentlyFilteredBooks = groupedBooks;

    recalculatePages();
    showCurrentPage();
}

int BookResultsViewModel::getCurrentBooksCount()
{
    return myReceivedBooks.size();
}

void BookResultsViewModel::newBooksReceived(QVector<Book*>& newBooks)
{

    for (int i = 0; i < myReceivedBooks.size(); i++)
    {
        BookResultViewModel* nextElement = myReceivedBooks.at(i);
        delete nextElement;
    }

    myReceivedBooks.clear();

    for (int i = 0; i < newBooks.size(); i++)
    {
        Book* nextBook = newBooks.at(i);
        BookResultViewModel* newBookVm = new BookResultViewModel(nextBook, this);

        myReceivedBooks.append(newBookVm);
    }

    updateShownBooks();
}

void BookResultsViewModel::bookInfoRequested(Book* book)
{
    emit infoOpenRequested(book);
}

void BookResultsViewModel::setConnections()
{
    connect(BookSearchManager::getInstance(), SIGNAL(booksChanged(QVector<Book*>&)), this, SLOT(newBooksReceived(QVector<Book*>&)));
}
