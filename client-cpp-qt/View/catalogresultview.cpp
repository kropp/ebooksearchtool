#include "catalogresultview.h"
#include "../ViewModel/catalogresultviewmodel.h"

#include <QHBoxLayout>
#include <QLabel>
#include <QPushButton>
#include <QFont>
#include <QFile>
#include <QDebug>

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
//    catalogPictureLabel = new QLabel(this);
//    catalogPictureLabel->setObjectName("catalogPictureLabel");

    catalogTitleLabel = new QLabel(viewModel->getCatalogName());
    catalogTitleLabel->setObjectName("catalogTitleLabel");

    catalogSummaryLabel = new QLabel(viewModel->getCatalogSummary());
    catalogSummaryLabel->setObjectName("catalogSummaryLabel");

    myOpenCatalogButton = new QPushButton(this);
    myOpenCatalogButton->setObjectName("openButton");
}

void CatalogResultView::layoutComponents()
{
    QHBoxLayout* bookLineLayout = new QHBoxLayout();

    QVBoxLayout* textLayout = new QVBoxLayout();

    textLayout->addSpacing(20);
    textLayout->addWidget(catalogTitleLabel);
    textLayout->addWidget(catalogSummaryLabel);
    textLayout->addSpacing(10);

    bookLineLayout->addSpacing(10);
    bookLineLayout->addWidget(myOpenCatalogButton);
    bookLineLayout->addSpacing(20);
    bookLineLayout->addLayout(textLayout);
//    bookLineLayout->addStretch(1);
//    bookLineLayout->addWidget(openButton);

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
    connect(myOpenCatalogButton, SIGNAL(clicked()), this, SLOT(openButtonPressed()));
}


void CatalogResultView::openButtonPressed()
{
    viewModel->openCatalogRequested();
}
