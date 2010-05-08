#include "bookresultslibraryview.h"

BookResultsLibraryView::BookResultsLibraryView
(
    QWidget* parent,
    LibraryBookResultsViewModel* resultsViewModel
)
    :BookResultsRearrangeView(parent, (BookResultsViewModel*)resultsViewModel, false)
{
}

BookResultsLibraryView::~BookResultsLibraryView()
{

}
