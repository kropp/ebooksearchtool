#ifndef BOOKRESULTSCATALOGVIEW_H
#define BOOKRESULTSCATALOGVIEW_H

#include "bookresultsrearrangeview.h"

#include <QWidget>

class CatalogBookResultsViewModel;

class BookResultsCatalogView : public BookResultsRearrangeView
{
    Q_OBJECT

    public:

        BookResultsCatalogView(QWidget* parent, CatalogBookResultsViewModel* resultsViewModel);
        ~BookResultsCatalogView();
};

#endif // BOOKRESULTSCATALOGVIEW_H
