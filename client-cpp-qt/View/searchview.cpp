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
    myViewModel = searchViewModel;

    initialize();

    myLastSearchString = "";
}

SearchView::~SearchView()
{

}

void SearchView::createComponents()
{
    StandardContentView::createComponents();

    mySearchLabel = new QLabel(this);
    mySearchLine = new QLineEdit("", this);
    mySearchButton = new QPushButton(this);
    myMoreButton = new QPushButton(this);

    mySearchLabel->setObjectName("searchLabel");
    mySearchLine->setObjectName("searchLine");
    mySearchButton->setObjectName("searchButton");
    myMoreButton->setObjectName("moreButton");

    myMoreButton->hide();

    myBookResults = new BookResultsRearrangeView(this, myViewModel->getBookResultsViewModel(), true);
    myBookFilter = new SelectionFilterView(this, myViewModel->getBookResultsViewModel());
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
    connect(myViewModel, SIGNAL(resultsAvailabilityChanged(bool)), this, SLOT(moreAvailabilityChanged(bool)));
    connect(mySearchButton, SIGNAL(clicked()), this, SLOT(goButtonPressed()));
    connect(myMoreButton, SIGNAL(clicked()), myViewModel, SLOT(moreBooksRequested()));
    connect(mySearchLine, SIGNAL(textEdited(QString)), this, SLOT(textEdited(QString)));
}

void SearchView::viewModelSearchResultsVisibilityChanged(bool /*visibility*/)
{

}

void SearchView::goButtonPressed()
{
    myViewModel->searchStartRequested(mySearchLine->text());
    myLastSearchString = mySearchLine->text();
    myMoreButton->setVisible(true);
    mySearchButton->setVisible(false);
}

void SearchView::addItemsToLeftBarPartLayout(QHBoxLayout* leftPartLayout)
{
    leftPartLayout->addWidget(mySearchLabel);
}

void SearchView::addItemsToRightBarPartLayout(QHBoxLayout* rightPartLayout)
{
    rightPartLayout->addWidget(mySearchLine);
    rightPartLayout->addSpacing(20);
    rightPartLayout->addWidget(mySearchButton);
    rightPartLayout->addWidget(myMoreButton);
}

void SearchView::addItemsToLeftContentPartLayout(QHBoxLayout* leftPartLayout)
{
    leftPartLayout->addWidget(myBookFilter);
}

void SearchView::addItemsToRightContentPartLayout(QHBoxLayout* rightPartLayout)
{
    rightPartLayout->addWidget(myBookResults);
}

void SearchView::textEdited(QString newText)
{
    bool availability = newText.length() > 0 && !newText.compare(myLastSearchString);
    myMoreButton->setVisible(availability);
    mySearchButton->setVisible(!availability);
}
