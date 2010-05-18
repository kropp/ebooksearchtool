#ifndef BOOKRESULTSVIEW_H
#define BOOKRESULTSVIEW_H

#include <QVector>
#include <QVBoxLayout>
#include <QHBoxLayout>

class BookResultsViewModel;
class BookResultViewModel;
class BookResultView;

#include "standardview.h"

class BookResultsView : public StandardView
{

    Q_OBJECT

    public:
        BookResultsView(QWidget* parent, BookResultsViewModel* resultsViewModel, bool addToLibrary);
        ~BookResultsView();

    protected:

        void createComponents();
        void layoutComponents();
        void setWindowParameters();
        void setConnections();

    public slots:

        void shownBooksChanged(QVector<BookResultViewModel*> newBooks);
        void changeSelectedBook(BookResultView* );

    private:

        void relayout();

    private:

        BookResultsViewModel* myViewModel;

        QVector<BookResultView*> myShownResults;
        QVector<BookResultViewModel*> myCurrentResultVms;

        BookResultView* mySelectedBook;

        QVBoxLayout* myCurrentVerticalLayout;

        bool myAddToLibraryResults;

};

#endif // BOOKRESULTSVIEW_H
