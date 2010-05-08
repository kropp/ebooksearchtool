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

    protected slots:

        void shownBooksChanged(QVector<BookResultViewModel*> newBooks);

    private:

        void relayout();

    private:

        BookResultsViewModel* myViewModel;

        QVector<BookResultView*> myShownResults;
        QVector<BookResultViewModel*> myCurrentResultVms;

        QVBoxLayout* myCurrentVerticalLayout;

        bool myAddToLibraryResults;

};

#endif // BOOKRESULTSVIEW_H
