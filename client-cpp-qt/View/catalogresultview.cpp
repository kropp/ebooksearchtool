#include "catalogresultview.h"
#include "../ViewModel/catalogresultviewmodel.h"

#include <QHBoxLayout>
#include <QLabel>
#include <QPushButton>
#include <QFont>
#include <QFile>

CatalogResultView::CatalogResultView(QWidget* parent, CatalogResultViewModel* bookResultViewModel) : StandardView(parent)
{
    viewModel = bookResultViewModel;

    initialize();
}

CatalogResultView::~CatalogResultView()
{

}

void CatalogResultView::createComponents()
{
    catalogPictureLabel = new QLabel(this);
    catalogPictureLabel->setObjectName("catalogPictureLabel");

    catalogTitleLabel = new QLabel(viewModel->getCatalogName());
    catalogTitleLabel->setObjectName("catalogTitleLabel");

    openButton = new QPushButton(this);
    openButton->setObjectName("openButton");
}

void CatalogResultView::layoutComponents()
{
    QHBoxLayout* bookLineLayout = new QHBoxLayout;

    bookLineLayout->addSpacing(10);
    bookLineLayout->addWidget(catalogPictureLabel);
    bookLineLayout->addSpacing(20);
    bookLineLayout->addWidget(catalogTitleLabel);
    bookLineLayout->addStretch(1);
    bookLineLayout->addWidget(openButton);

    this->setLayout(bookLineLayout);
}

void CatalogResultView::setWindowParameters()
{
    QFile styleSheetFile(":/qss/CatalogResultStyle");
    styleSheetFile.open(QIODevice::ReadOnly);
    this->setStyleSheet(styleSheetFile.readAll());
}

void CatalogResultView::setConnections()
{
    connect(openButton, SIGNAL(clicked()), this, SLOT(openButtonPressed()));
}


void CatalogResultView::resizeEvent(QResizeEvent* event)
{

}


void CatalogResultView::openButtonPressed()
{
    viewModel->openCatalogRequested();
}
