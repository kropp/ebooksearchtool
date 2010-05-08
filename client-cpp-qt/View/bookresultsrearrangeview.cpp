#include "bookresultsrearrangeview.h"
#include "bookresultview.h"
#include "bookresultsview.h"
#include "pageview.h"

#include "../ViewModel/bookresultviewmodel.h"
#include "../ViewModel/bookresultsviewmodel.h"

#include <QFile>

static const int WHOLE_SCROLL_CONTENTS_WIDTH = 800;
static const int WHOLE_SCROLL_CONTENT_LINE_HEIGHT = 80;

static const int SCROLL_AREA_CONTENTS_MARGIN = 20;

BookResultsRearrangeView::BookResultsRearrangeView
(
    QWidget* parent,
    BookResultsViewModel* bookResultViewModel,
    bool addToLibrary
) : StandardView(parent)
{
    myResultsAddToLibrary = addToLibrary;
    viewModel = bookResultViewModel;
    initialize();
}

BookResultsRearrangeView::~BookResultsRearrangeView()
{

}

void BookResultsRearrangeView::createComponents()
{
    myScrollArea = new QScrollArea(this);
    resultsView = new BookResultsView(this, viewModel, myResultsAddToLibrary);

    myScrollArea->setWidget(resultsView);
    myScrollArea->setObjectName("scrollArea");

    myPageView = new PageView(this, viewModel);
}

void BookResultsRearrangeView::layoutComponents()
{
    QVBoxLayout* mainLayout = new QVBoxLayout();

    mainLayout->setMargin(0);
    mainLayout->setSpacing(0);
    mainLayout->addWidget(myScrollArea);
    mainLayout->addWidget(myPageView);

    this->setLayout(mainLayout);
}

void BookResultsRearrangeView::setWindowParameters()
{
    QFile styleSheetFile(":/qss/RearrangeViewStyle");
    styleSheetFile.open(QIODevice::ReadOnly);
    this->setStyleSheet(styleSheetFile.readAll());
}

void BookResultsRearrangeView::setConnections()
{
    connect(viewModel, SIGNAL(shownBooksChanged(QVector<BookResultViewModel*>)), this, SLOT(shownBooksChanged(QVector<BookResultViewModel*>)));
}

void BookResultsRearrangeView::resizeEvent(QResizeEvent* event)
{
    int newWidth = event->size().width();
    resultsView->setFixedWidth(newWidth - SCROLL_AREA_CONTENTS_MARGIN);
}

void BookResultsRearrangeView::shownBooksChanged(QVector<BookResultViewModel*> newBooks)
{
    resultsView->setFixedHeight(WHOLE_SCROLL_CONTENT_LINE_HEIGHT * newBooks.size());
}
