#include "bookresultsview.h"
#include "bookresultview.h"

#include <QLabel>

#include "../ViewModel/bookresultviewmodel.h"
#include "../ViewModel/bookresultsviewmodel.h"

BookResultsView::BookResultsView
(
    QWidget* parent,
    BookResultsViewModel* bookResultsViewModel,
    bool addToLibrary
) : StandardView(parent)
{
    myAddToLibraryResults = addToLibrary;
    myCurrentVerticalLayout = 0;
    myViewModel = bookResultsViewModel;
    mySelectedBook = 0;
    initialize();
}

BookResultsView::~BookResultsView()
{

}

void BookResultsView::createComponents()
{
}

void BookResultsView::layoutComponents()
{
    relayout();
}

void BookResultsView::relayout()
{
    if (myCurrentVerticalLayout)
    {
        delete myCurrentVerticalLayout;
    }

    myCurrentVerticalLayout = new QVBoxLayout(this);

    myCurrentVerticalLayout->setSpacing(-1);
    myCurrentVerticalLayout->setMargin(0);
    myCurrentVerticalLayout->setContentsMargins(20, 0, 0, 0);

    for (int i = 0; i < myShownResults.size(); i++)
    {
        BookResultView* nextElement = myShownResults.at(i);
        myCurrentVerticalLayout->addWidget(nextElement);
        nextElement->show();
    }

    this->setLayout(myCurrentVerticalLayout);

}

void BookResultsView::setWindowParameters()
{
    QPalette pal = this->palette();
    pal.setColor(QPalette::Background, Qt::white);
    this->setPalette(pal);
    this->setAutoFillBackground(true);
}

void BookResultsView::setConnections()
{
    connect(myViewModel, SIGNAL(shownBooksChanged(QVector<BookResultViewModel*>)), this, SLOT(shownBooksChanged(QVector<BookResultViewModel*>)));
}

void BookResultsView::shownBooksChanged(QVector<BookResultViewModel*> newBooks)
{
    for (int i = 0; i < myShownResults.size(); i++)
    {
        BookResultView* nextElement = myShownResults.at(i);
        delete nextElement;
    }

    myShownResults.clear();

    for (int i = 0; i < newBooks.size(); i++)
    {
        BookResultViewModel* nextBookVm = newBooks.at(i);
        BookResultView* newBookView =  new BookResultView(this, nextBookVm, myAddToLibraryResults);
        myShownResults.append(newBookView);
    }

    mySelectedBook = 0;
    relayout();
}

void BookResultsView::changeSelectedBook(BookResultView* newBookView) {
    if (mySelectedBook) {
        mySelectedBook->cancelSelection();
    }
    mySelectedBook = newBookView;
    mySelectedBook->select();
    //myViewModel->bookInfoRequested(newBookView);
}
