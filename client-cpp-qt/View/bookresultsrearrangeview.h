#ifndef BOOKRESULTSREARRANGEVIEW_H
#define BOOKRESULTSREARRANGEVIEW_H

#include "../ViewModel/bookresultsviewmodel.h"

#include <QVector>
#include <QVBoxLayout>
#include <QHBoxLayout>
#include <QLabel>
#include <QScrollArea>
#include <QComboBox>
#include <QLineEdit>
#include <QPushButton>

class BookResultViewModel;
class BookResultView;
class BookResultsView;
class PageView;

#include "standardview.h"


class BookResultsRearrangeView : public StandardView
{

    Q_OBJECT

    public:

        BookResultsRearrangeView(QWidget* parent, BookResultsViewModel* resultsViewModel, bool addToLibrary);
        ~BookResultsRearrangeView();

    protected:

        void createComponents();
        void layoutComponents();
        void setWindowParameters();
        void setConnections();
        void resizeEvent(QResizeEvent* event);

    protected slots:

        void shownBooksChanged(QVector<BookResultViewModel*> newBooks);

    protected:

        BookResultsViewModel* viewModel;
        BookResultsView* resultsView;

    private:

        QScrollArea* myScrollArea;

        QVBoxLayout* myCurrentVerticalLayout;

        PageView* myPageView;

        bool myResultsAddToLibrary;
};

#endif // BOOKRESULTSREARRANGEVIEW_H
