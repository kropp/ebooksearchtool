#include "searchview.h"
#include "bookresultsrearrangeview.h"
#include "../ViewModel/searchviewmodel.h"
#include "../Model/booksearchmanager.h"
#include <QPushButton>
#include <QLineEdit>
#include <QLabel>
#include <QVBoxLayout>
#include <QHBoxLayout>
#include <QPixmap>
#include <QIcon>
#include <QDebug>
#include <QFile>
#include <QProgressBar>
#include <QKeyEvent>

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
    myProgressBar = new QProgressBar(this);
    myProgressBar->setMaximum(0);
    myProgressBar->setMinimum(0);

    searchButton->setCursor(Qt::PointingHandCursor);
    moreButton->setCursor(Qt::PointingHandCursor);

    searchLabel->setObjectName("searchLabel");
    searchLine->setObjectName("searchLine");
    searchButton->setObjectName("searchButton");
    moreButton->setObjectName("moreButton");
    myProgressBar->setObjectName("progressBar");

    moreButton->hide();
    myProgressBar->hide();

    myBookResults = new BookResultsRearrangeView(this, viewModel->getBookResultsViewModel(), true);
    myBookFilter = new SelectionFilterView(this, viewModel->getBookResultsViewModel());
    myInformationView = new InformationView(this, viewModel->getBookInfoViewModel());
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
    connect(moreButton, SIGNAL(clicked()), this, SLOT(showProgress()));
    connect(BookSearchManager::getInstance(), SIGNAL(downloadFinished()), this, SLOT(hideProgress()));
    connect(searchLine, SIGNAL(textEdited(QString)), this, SLOT(textEdited(QString)));
}

void SearchView::viewModelSearchResultsVisibilityChanged(bool /*visibility*/)
{

}

void SearchView::moreAvailabilityChanged(bool availability) {
     moreButton->setEnabled(availability);
}

void SearchView::keyPressEvent(QKeyEvent* event)
{
    int key = event->key();
    if (key == Qt::Key_Enter ||
        key == Qt::Key_Return)
    {
        goButtonPressed();
    }
}

void SearchView::goButtonPressed()
{
    if (searchLine->text().isEmpty()) {
        return;
    }
    viewModel->searchStartRequested(searchLine->text());
    lastSearchString = searchLine->text();
    moreButton->setVisible(true);
    searchButton->setVisible(false);
    showProgress();
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
    rightPartLayout->addWidget(myProgressBar);
}

void SearchView::addItemsToLeftContentPartLayout(QHBoxLayout* leftPartLayout)
{
    QVBoxLayout* mainLayout = new QVBoxLayout();
    mainLayout->addWidget(myBookFilter);
    mainLayout->addWidget(myInformationView);
    leftPartLayout->addLayout(mainLayout);
}

void SearchView::addItemsToRightContentPartLayout(QHBoxLayout* rightPartLayout)
{
    rightPartLayout->addWidget(myBookResults);
}

void SearchView::textEdited(QString newText)
{
    bool availability = newText.length() > 0 && !newText.compare(lastSearchString);
    moreButton->setVisible(availability);
    searchButton->setVisible(!availability);
}

void SearchView::showProgress() {
    myProgressBar->show();
}

void SearchView::hideProgress() {
    myProgressBar->hide();
}
