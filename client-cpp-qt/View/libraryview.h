#ifndef LIBRARYVIEW_H
#define LIBRARYVIEW_H

#include "standardview.h"
#include "standardcontentview.h"
#include "selectionfilterview.h"

#include <QLabel>
#include <QHBoxLayout>

class LibraryViewModel;
class BookResultsLibraryView;

class LibraryView : public StandardContentView
{

    Q_OBJECT

public:

    LibraryView(QWidget* parent, LibraryViewModel* newViewModel);

protected:

    void createComponents();
    void layoutComponents();
    void setWindowParameters();
    void setConnections();

    virtual void addItemsToLeftBarPartLayout(QHBoxLayout* leftPartLayout);
    virtual void addItemsToRightBarPartLayout(QHBoxLayout* rightPartLayout);
    virtual void addItemsToLeftContentPartLayout(QHBoxLayout* leftPartLayout);
    virtual void addItemsToRightContentPartLayout(QHBoxLayout* rightPartLayout);

private slots:

    void viewModelSearchResultsVisibilityChanged(bool availability);

private:

    QLabel* libraryLabel;

    LibraryViewModel* viewModel;

    BookResultsLibraryView* libraryViewResults;
    SelectionFilterView* bookFilter;
};

#endif // LIBRARYVIEW_H
