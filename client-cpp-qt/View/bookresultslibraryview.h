#ifndef BOOKRESULTSLIBRARYVIEW_H
#define BOOKRESULTSLIBRARYVIEW_H

#include "bookresultsrearrangeview.h"

#include <QWidget>

class LibraryBookResultsViewModel;

class BookResultsLibraryView : public BookResultsRearrangeView
{
    Q_OBJECT

    public:

        BookResultsLibraryView(QWidget* parent, LibraryBookResultsViewModel* resultsViewModel);
        ~BookResultsLibraryView();
};

#endif // BOOKRESULTSLIBRARYVIEW_H
