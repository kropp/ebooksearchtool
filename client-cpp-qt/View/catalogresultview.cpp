#include "catalogresultview.h"
#include "../ViewModel/catalogresultviewmodel.h"
#include "catalogresultsview.h"


#include <QHBoxLayout>
#include <QLabel>
#include <QPushButton>
#include <QFont>
#include <QFile>
#include <QDebug>

CatalogResultView::CatalogResultView(CatalogResultsView* parent, CatalogResultViewModel* bookResultViewModel) : StandardView((QWidget*)parent)
{
    myViewModel = bookResultViewModel;
    myParent = parent;
    initialize();
}

CatalogResultView::~CatalogResultView()
{

}

void CatalogResultView::createComponents()
{
//    catalogPictureLabel = new QLabel(this);
//    catalogPictureLabel->setObjectName("catalogPictureLabel");

    myCatalogTitleLabel = new QLabel(myViewModel->getCatalogName());
    myCatalogTitleLabel->setCursor(Qt::PointingHandCursor);
    myCatalogTitleLabel->setObjectName("catalogTitleLabel");

    myCatalogSummaryLabel = new QLabel(myViewModel->getCatalogSummary());
    myCatalogSummaryLabel->setObjectName("catalogSummaryLabel");

    myOpenCatalogButton = new QPushButton(this);
    myOpenCatalogButton->setCursor(Qt::PointingHandCursor);
    myOpenCatalogButton->setObjectName("openButton");
}

void CatalogResultView::layoutComponents()
{
    QHBoxLayout* bookLineLayout = new QHBoxLayout();

    QVBoxLayout* textLayout = new QVBoxLayout();

    textLayout->addSpacing(20);
    textLayout->addWidget(myCatalogTitleLabel);
    textLayout->addWidget(myCatalogSummaryLabel);
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
    connect(myOpenCatalogButton, SIGNAL(clicked()), myParent, SIGNAL(requestToOpenCatalog()));
    connect(this, SIGNAL(openCatalogRequested()), myParent, SIGNAL(requestToOpenCatalog()));
    connect(this, SIGNAL(openCatalogRequested()), this, SLOT(openButtonPressed()));

}

void CatalogResultView::mousePressEvent(QMouseEvent *) {
    emit openCatalogRequested();
}

void CatalogResultView::openButtonPressed()
{
    myViewModel->openCatalogRequested();
}
