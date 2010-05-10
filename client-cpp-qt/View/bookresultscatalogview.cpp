#include "bookresultscatalogview.h"

BookResultsCatalogView::BookResultsCatalogView
(
    QWidget* parent,
    CatalogBookResultsViewModel* resultsViewModel
)
:BookResultsRearrangeView(parent, (BookResultsViewModel*)resultsViewModel, true)
{
}

BookResultsCatalogView::~BookResultsCatalogView()
{

}
