#include "catalogview.h"
#include "bookresultscatalogview.h"
#include "catalogresultsview.h"
#include "catalogbrowsebarpanel.h"

#include <QVBoxLayout>
#include <QHBoxLayout>

#include <QIcon>
#include <QFile>

#include "../ViewModel/catalogviewmodel.h"
#include "../ViewModel/catalogbookresultsviewmodel.h"
#include "../ViewModel/catalogresultsviewmodel.h"


static const int WHOLE_SCROLL_CONTENT_LINE_HEIGHT = 100;
static const int SCROLL_AREA_CONTENTS_MARGIN = 320;

CatalogView::CatalogView(QWidget* parent, CatalogViewModel* newViewModel) : StandardContentView(parent)
{
    viewModel = newViewModel;

    initialize();
    folderCatalogViewModelChanged();
}

void CatalogView::resizeEvent(QResizeEvent* event)
{
    int newWidth = event->size().width();
    catalogView->setFixedWidth(newWidth - SCROLL_AREA_CONTENTS_MARGIN);
}

void CatalogView::booksCatalogViewModelChanged()
{
    catalogView->hide();
    scrollArea->hide();
    booksView->show();
}

void CatalogView::folderCatalogViewModelChanged()
{
    int linesCount = viewModel->getFolderResultsViewModel()->getShownCatalogs()->size();

    booksView->hide();
    catalogView->show();
    scrollArea->show();

    catalogView->setFixedHeight(WHOLE_SCROLL_CONTENT_LINE_HEIGHT * linesCount);
}

void CatalogView::createComponents()
{
    StandardContentView::createComponents();

    catalogLabel = new QLabel(this);
    catalogLabel->setObjectName("catalogLabel");

    scrollArea = new QScrollArea(this);
    scrollArea->setObjectName("scrollArea");


    booksView = new BookResultsCatalogView(this, viewModel->getBookResultsViewModel());

    catalogView = new CatalogResultsView(this, viewModel->getFolderResultsViewModel());

    scrollArea->setWidget(catalogView);

    browseBar = new CatalogBrowseBarPanel(this, viewModel->getBrowseBarPanelModel());
}

void CatalogView::layoutComponents()
{
    StandardContentView::layoutComponents();
}

void CatalogView::setWindowParameters()
{
    StandardContentView::setWindowParameters();

    QFile styleSheetFile(":/qss/CatalogStyle");
    styleSheetFile.open(QIODevice::ReadOnly);
    this->setStyleSheet(this->styleSheet() + styleSheetFile.readAll());
}

void CatalogView::setConnections()
{
    connect(viewModel, SIGNAL(CatalogBookResultsChanged()), this, SLOT(booksCatalogViewModelChanged()));
    connect(viewModel, SIGNAL(CatalogFolderResultsChanged()), this, SLOT(folderCatalogViewModelChanged()));
}

void CatalogView::addItemsToLeftBarPartLayout(QHBoxLayout* leftPartLayout)
{
    leftPartLayout->addWidget(catalogLabel);
}

void CatalogView::addItemsToRightBarPartLayout(QHBoxLayout* rightPartLayout)
{
    rightPartLayout->addWidget(browseBar);
}


void CatalogView::addItemsToLeftContentPartLayout(QHBoxLayout* leftPartLayout)
{

}

void CatalogView::addItemsToRightContentPartLayout(QHBoxLayout* rightPartLayout)
{
    rightPartLayout->addWidget(scrollArea);
    rightPartLayout->addWidget(booksView);
}
