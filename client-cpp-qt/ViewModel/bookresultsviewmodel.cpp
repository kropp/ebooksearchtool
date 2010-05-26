#include "bookresultsviewmodel.h"
#include "bookresultviewmodel.h"

#include "../Model/book.h"
#include "../Model/booksearchmanager.h"
#include "../Model/settingsmanager.h"

#include <QDebug>

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
    int resultsOnPage = SettingsManager::getInstance()->getBooksOnPage();

    int totalCount = myCurrentlyFilteredBooks.size();
    bool additionalPageNeeded = (totalCount % resultsOnPage);
    int fullPagesCount = (totalCount / resultsOnPage) + (additionalPageNeeded ? 1 : 0);
    int pagesCountAfterWindowPosition = fullPagesCount - myPageWindowIndex * PAGES_WINDOW_SIZE;

    if (pagesCountAfterWindowPosition < 0)
    {
        myPageWindowIndex = 0;
        pagesCountAfterWindowPosition = (totalCount / resultsOnPage) + (additionalPageNeeded ? 1 : 0);
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
    int resultsOnPage = SettingsManager::getInstance()->getBooksOnPage();

    QVector<BookResultViewModel*> currentPageBooks;

    for (int i = myCurrentPageWithPageWindowCorrection * resultsOnPage; i < (myCurrentPageWithPageWindowCorrection + 1) * resultsOnPage; i++)
    {
        if (i < myCurrentlyFilteredBooks.size())
        {
            currentPageBooks.append(myCurrentlyFilteredBooks.at(i));
            myCurrentlyFilteredBooks.at(i)->downloadCover();
        }
    }
    myCurrentSelectedBook = 0;
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
    default:
        break;
    }

    return term;
}

void BookResultsViewModel::updateShownBooks()
{

   QVector<BookResultViewModel*> processedBooks(myRecievedBooks);

    QVector<BookResultViewModel*> filteredBooks;
    
    if (myFilterType != NONE)
    {
        for (int i = 0; i < processedBooks .size(); i++)
        {
            BookResultViewModel* nextElement = processedBooks.at(i);
            QString filteredTerm = getTerm(nextElement, myFilterType);

            if (filteredTerm.toLower().contains(myFilterWords.toLower()))
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
        default:
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
    return myBooksMapping.size();
}

void BookResultsViewModel::newBooksReceived(QVector<Book*>& newBooks)
{
    // remove old books if new books don't contain them

    QList<Book*> oldBooks = myBooksMapping.keys();

    foreach (Book* oldBook, oldBooks) {
        if (!newBooks.contains(oldBook)) {
            BookResultViewModel* oldVm = myBooksMapping[oldBook];
            myBooksMapping.remove(oldBook);
            myRecievedBooks.remove(myRecievedBooks.indexOf(oldVm));
            delete oldVm;
        }
    }

    // create new Vms if they haven't already been created
    foreach (Book* book, newBooks) {
        if (!myBooksMapping.contains(book)) {
            BookResultViewModel* bookVm = new BookResultViewModel(book, this);
            myBooksMapping.insert(book, bookVm);
            myRecievedBooks.append(bookVm);
        }
    }

    updateShownBooks();
}

void BookResultsViewModel::bookInfoRequested(Book* book)
{
    qDebug() << "BookResultsViewModel::bookInfoRequested();" << book->getTitle();
    emit infoOpenRequested(book);
}

void BookResultsViewModel::setConnections()
{
    connect(BookSearchManager::getInstance(), SIGNAL(booksChanged(QVector<Book*>&)), this, SLOT(newBooksReceived(QVector<Book*>&)));
}
