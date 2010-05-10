#include "searchview.h"
#include "bookresultsrearrangeview.h"
#include "../ViewModel/searchviewmodel.h"
#include <QPushButton>
#include <QLineEdit>
#include <QLabel>
#include <QVBoxLayout>
#include <QHBoxLayout>
#include <QPixmap>
#include <QIcon>
#include <QDebug>
#include <QFile>

SearchView::SearchView(QWidget* parent, SearchViewModel* searchViewModel) : StandardContentView(parent)
{
    viewModel = searchViewModel;

    initialize();

    lastSearchString = "";
}

SearchView::~SearchView()
{

}

void SearchView::createComponents()
{
    StandardContentView::createComponents();

    searchLabel = new QLabel(this);
    searchLine = new QLineEdit("", this);
    searchButton = new QPushButton(this);
    moreButton = new QPushButton(this);

    searchLabel->setObjectName("searchLabel");
    searchLine->setObjectName("searchLine");
    searchButton->setObjectName("searchButton");
    moreButton->setObjectName("moreButton");

    moreButton->hide();

    bookResults = new BookResultsRearrangeView(this, viewModel->getBookResultsViewModel(), true);
    bookFilter = new SelectionFilterView(this, viewModel->getBookResultsViewModel());
}

void SearchView::layoutComponents()
{
    StandardContentView::layoutComponents();
}

void SearchView::setWindowParameters()
{
    StandardContentView::setWindowParameters();

    QFile styleSheetFile(":/qss/SearchStyle");
    styleSheetFile.open(QIODevice::ReadOnly);
    this->setStyleSheet(this->styleSheet() + styleSheetFile.readAll());
}

void SearchView::setConnections()
{
    connect(viewModel, SIGNAL(resultsAvailabilityChanged(bool)), this, SLOT(moreAvailabilityChanged(bool)));
    connect(searchButton, SIGNAL(clicked()), this, SLOT(goButtonPressed()));
    connect(moreButton, SIGNAL(clicked()), viewModel, SLOT(moreBooksRequested()));
    connect(searchLine, SIGNAL(textEdited(QString)), this, SLOT(textEdited(QString)));
}

void SearchView::viewModelSearchResultsVisibilityChanged(bool visibility)
{

}

void SearchView::goButtonPressed()
{
    viewModel->searchStartRequested(searchLine->text());
    lastSearchString = searchLine->text();
    moreButton->setVisible(true);
    searchButton->setVisible(false);
}

void SearchView::addItemsToLeftBarPartLayout(QHBoxLayout* leftPartLayout)
{
    leftPartLayout->addWidget(searchLabel);
}

void SearchView::addItemsToRightBarPartLayout(QHBoxLayout* rightPartLayout)
{
    rightPartLayout->addWidget(searchLine);
    rightPartLayout->addSpacing(20);
    rightPartLayout->addWidget(searchButton);
    rightPartLayout->addWidget(moreButton);
}

void SearchView::addItemsToLeftContentPartLayout(QHBoxLayout* leftPartLayout)
{
    leftPartLayout->addWidget(bookFilter);
}

void SearchView::addItemsToRightContentPartLayout(QHBoxLayout* rightPartLayout)
{
    rightPartLayout->addWidget(bookResults);
}

void SearchView::textEdited(QString newText)
{
    bool availability = newText.length() > 0 && !newText.compare(lastSearchString);
    moreButton->setVisible(availability);
    searchButton->setVisible(!availability);
}
