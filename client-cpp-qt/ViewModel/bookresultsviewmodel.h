#ifndef BOOKRESULTSVIEWMODEL_H
#define BOOKRESULTSVIEWMODEL_H

#include <QObject>
#include <QVector>
#include <QList>

class Book;
class BookResultViewModel;

enum SelectionType { NONE, AUTHOR, LANGUAGE, SERVER };

class BookResultsViewModel : public QObject
{

Q_OBJECT

public:

    BookResultsViewModel();

public:

    void initialize();
    int getCurrentBooksCount();

public slots:

    void changeGroupingType(SelectionType newType);
    void changeSortingType(SelectionType newType);
    void changeFilteringType(SelectionType newType);
    void changeFilteringPhrase(QString filter);
    void changeAllFilteringDataSimultaneously
    (
        SelectionType newGroupType,
        SelectionType newSortType,
        SelectionType newFilterType,
        QString newFilterTerm
    );
    void requestToChangePage(int page);
    void requestNextPagesWindow();
    void requestPrevPagesWindow();
    void bookInfoRequested(Book* book);

signals:

    void pageChanged(int page);
    void shownBooksChanged(QVector<BookResultViewModel*> newBooks);
    void pagesCountChanged(int pagesCountChanged, int startingPage);
    void pagePrevAvailabilityChanged(bool availability);
    void pageNextAvailabilityChanged(bool availability);
    void infoOpenRequested(Book* book);

protected:

    void setConnections();

protected slots:

    void newBooksReceived(QVector<Book*>& newBooks);

private:
    
    void updateShownBooks();
    QString getTerm(BookResultViewModel* element, SelectionType selection);
    bool sortingLessThan(BookResultViewModel* s1, BookResultViewModel* s2);

    void recalculatePages();
    void showCurrentPage();

private:

    QVector<BookResultViewModel*> receivedBooks;
    QVector<BookResultViewModel*> currentlyFilteredBooks;

    QString filterWords;

    SelectionType groupType;
    SelectionType sortType;
    SelectionType filterType;

    int currentPageWithPageWindowCorrection;
    int currentPageWithoutPageWindowCorrection;

    int pageWindowIndex;
};

#endif // BOOKRESULTSVIEWMODEL_H
