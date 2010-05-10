#include "catalogresultsview.h"
#include "catalogresultview.h"

#include <QLabel>

#include "../ViewModel/catalogresultviewmodel.h"
#include "../ViewModel/catalogresultsviewmodel.h"



CatalogResultsView::CatalogResultsView
(
    QWidget* parent,
    CatalogResultsViewModel* catalogResultsViewModel
) : StandardView(parent)
{
    currentVerticalLayout = 0;
    viewModel = catalogResultsViewModel;
    shownResults = QVector<CatalogResultView*>();
    initialize();
    createChildViews(viewModel->getShownCatalogs());
}

CatalogResultsView::~CatalogResultsView()
{

}

void CatalogResultsView::createComponents()
{
}

void CatalogResultsView::layoutComponents()
{
    //relayout();
}

void CatalogResultsView::relayout()
{
    if (currentVerticalLayout)
    {
        delete currentVerticalLayout;
    }

    currentVerticalLayout = new QVBoxLayout(this);

    currentVerticalLayout->setSpacing(0);
    currentVerticalLayout->setMargin(0);
    currentVerticalLayout->addSpacing(10);
    //currentVerticalLayout->setContentsMargins(20, 0, 0, 0);

    for (int i = 0; i < shownResults.size(); i++)
    {
        CatalogResultView* nextElement = shownResults.at(i);
        currentVerticalLayout->addWidget(nextElement);
        nextElement->show();
    }

    this->setLayout(currentVerticalLayout);

}

void CatalogResultsView::setWindowParameters()
{

}

void CatalogResultsView::setConnections()
{
    connect(viewModel, SIGNAL(shownCatalogsChanged(QVector<CatalogResultViewModel*>*)), this, SLOT(createChildViews(QVector<CatalogResultViewModel*>*)));
}

void CatalogResultsView::createChildViews(QVector<CatalogResultViewModel*>* childModels)
{
    for (int i = 0; i < shownResults.size(); i++)
    {
        CatalogResultView* nextElement = shownResults.at(i);
        delete nextElement;
    }

    shownResults.clear();

    for (int i = 0; i < childModels->size(); i++)
    {
        CatalogResultViewModel* nextCatalogVm = childModels->at(i);
        CatalogResultView* newBookView =  new CatalogResultView(this, nextCatalogVm);
        shownResults.append(newBookView);
    }

    relayout();
}

