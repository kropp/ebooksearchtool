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
    receivedBooks = QVector<BookResultViewModel*>();
    currentlyFilteredBooks = QVector<BookResultViewModel*>();

    currentPageWithPageWindowCorrection = 0;
    currentPageWithoutPageWindowCorrection = 0;
    pageWindowIndex = 0;

    recalculatePages();
}

void BookResultsViewModel::initialize()
{
    setConnections();
}

void BookResultsViewModel::requestNextPagesWindow()
{
    pageWindowIndex++;
    recalculatePages();
}

void BookResultsViewModel::requestPrevPagesWindow()
{
    pageWindowIndex--;
    recalculatePages();
}

void BookResultsViewModel::recalculatePages()
{
    int totalCount = currentlyFilteredBooks.size();
    bool additionalPageNeeded = (totalCount % RESULTS_ON_PAGE);
    int fullPagesCount = (totalCount / RESULTS_ON_PAGE) + (additionalPageNeeded ? 1 : 0);
    int pagesCountAfterWindowPosition = fullPagesCount - pageWindowIndex * PAGES_WINDOW_SIZE;

    if (pagesCountAfterWindowPosition < 0)
    {
        pageWindowIndex = 0;
        pagesCountAfterWindowPosition = (totalCount / RESULTS_ON_PAGE) + (additionalPageNeeded ? 1 : 0);
    }


    int pagesToBeShown = pagesCountAfterWindowPosition > PAGES_WINDOW_SIZE
                                        ? PAGES_WINDOW_SIZE
                                        : pagesCountAfterWindowPosition;

    if (currentPageWithoutPageWindowCorrection >= pagesToBeShown)
    {
        requestToChangePage(0);
    }
    else
    {
        requestToChangePage(currentPageWithoutPageWindowCorrection);
        showCurrentPage();
    }


    emit pagesCountChanged(pagesToBeShown, pageWindowIndex * PAGES_WINDOW_SIZE);
    emit pagePrevAvailabilityChanged(pageWindowIndex > 0);
    emit pageNextAvailabilityChanged(pagesCountAfterWindowPosition > PAGES_WINDOW_SIZE);
}

void BookResultsViewModel::requestToChangePage(int page)
{

    currentPageWithoutPageWindowCorrection = page;
    currentPageWithPageWindowCorrection = page + pageWindowIndex * PAGES_WINDOW_SIZE;

    emit pageChanged(page);
    showCurrentPage();
}

void BookResultsViewModel::showCurrentPage()
{
    QVector<BookResultViewModel*> currentPageBooks;

    for (int i = currentPageWithPageWindowCorrection * RESULTS_ON_PAGE; i < (currentPageWithPageWindowCorrection + 1) * RESULTS_ON_PAGE; i++)
    {
        if (i < currentlyFilteredBooks.size())
        {
            currentPageBooks.append(currentlyFilteredBooks.at(i));
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
    groupType = newGroupType;
    sortType = newSortType;
    filterType = newFilterType;
    filterWords = newFilterTerm;
    updateShownBooks();
}

void BookResultsViewModel::changeGroupingType(SelectionType newType)
{
    groupType = newType;
    updateShownBooks();
}

void BookResultsViewModel::changeSortingType(SelectionType newType)
{
    sortType = newType;
    updateShownBooks();
}

void BookResultsViewModel::changeFilteringType(SelectionType newType)
{
    filterType = newType;
    updateShownBooks();
}

void BookResultsViewModel::changeFilteringPhrase(QString filter)
{
    filterWords = filter;
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
    QVector<BookResultViewModel*> processedBooks = QVector<BookResultViewModel*>(receivedBooks);
    QVector<BookResultViewModel*> filteredBooks;
    
    if (filterType != NONE)
    {
        for (int i = 0; i < processedBooks .size(); i++)
        {
            BookResultViewModel* nextElement = processedBooks.at(i);
            QString filteredTerm = getTerm(nextElement, filterType);

            if (filteredTerm.contains(filterWords))
            {
                filteredBooks.append(nextElement);
            }
        }
    }
    else
    {
        filteredBooks = processedBooks;
    }

    if (sortType != NONE)
    {
        switch (sortType)
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

    if (groupType != NONE)
    {
        while(filteredBooks.size() > 0)
        {
            BookResultViewModel* firstElement = filteredBooks.at(0);
            groupedBooks.append(firstElement);

            QString groupTermFirst = getTerm(firstElement, groupType);

            for (int i = 1; i < filteredBooks .size(); i++)
            {
                BookResultViewModel* comparedElement = filteredBooks.at(i);

                QString groupTermCompared = getTerm(comparedElement, groupType);

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

    currentlyFilteredBooks = groupedBooks;

    recalculatePages();
    showCurrentPage();
}

int BookResultsViewModel::getCurrentBooksCount()
{
    return receivedBooks.size();
}

void BookResultsViewModel::newBooksReceived(QVector<Book*>& newBooks)
{

    for (int i = 0; i < receivedBooks.size(); i++)
    {
        BookResultViewModel* nextElement = receivedBooks.at(i);
        delete nextElement;
    }

    receivedBooks.clear();

    for (int i = 0; i < newBooks.size(); i++)
    {
        Book* nextBook = newBooks.at(i);
        BookResultViewModel* newBookVm = new BookResultViewModel(nextBook, this);

        receivedBooks.append(newBookVm);
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
