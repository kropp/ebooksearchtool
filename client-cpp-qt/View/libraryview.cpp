#include "libraryview.h"
#include "bookresultslibraryview.h"

#include "../ViewModel/libraryviewmodel.h"

#include <QVBoxLayout>
#include <QIcon>
#include <QFile>

static const QString NO_LIBRARY_RESULTS_BUTTON_ICON = ":/LibraryIsEmpty";

static const int WIDGET_MINIMAL_WIDTH = 400;

LibraryView::LibraryView(QWidget* parent, LibraryViewModel* newViewModel) : StandardContentView(parent)
{
    viewModel = newViewModel;

    initialize();
}

void LibraryView::createComponents()
{
    StandardContentView::createComponents();

    libraryLabel = new QLabel(this);
    libraryLabel->setObjectName("libraryLabel");

    libraryViewResults = new BookResultsLibraryView(this, viewModel->getBookResultsViewModel());
    bookFilter = new SelectionFilterView(this, (BookResultsViewModel*)viewModel->getBookResultsViewModel());
}

void LibraryView::layoutComponents()
{
    StandardContentView::layoutComponents();
}

void LibraryView::viewModelSearchResultsVisibilityChanged(bool visibility)
{

}

void LibraryView::setWindowParameters()
{
    StandardContentView::setWindowParameters();

    QFile styleSheetFile(":/qss/LibraryStyle");
    styleSheetFile.open(QIODevice::ReadOnly);
    this->setStyleSheet(this->styleSheet() + styleSheetFile.readAll());
}

void LibraryView::setConnections()
{
    connect(viewModel, SIGNAL(libraryResultsAvailabilityChanged(bool)), this, SLOT(viewModelSearchResultsVisibilityChanged(bool)));
}

void LibraryView::addItemsToLeftBarPartLayout(QHBoxLayout* leftPartLayout)
{
    leftPartLayout->addWidget(libraryLabel);
}

void LibraryView::addItemsToRightBarPartLayout(QHBoxLayout* rightPartLayout)
{

}


void LibraryView::addItemsToLeftContentPartLayout(QHBoxLayout* leftPartLayout)
{
    leftPartLayout->addWidget(bookFilter);
}

void LibraryView::addItemsToRightContentPartLayout(QHBoxLayout* rightPartLayout)
{
    rightPartLayout->addWidget(libraryViewResults);
}

