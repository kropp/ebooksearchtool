#ifndef CATALOGVIEW_H
#define CATALOGVIEW_H

#include <QWidget>
#include <QLabel>
#include <QScrollArea>
#include <QResizeEvent>

class CatalogViewModel;

class BookResultsCatalogView;
class CatalogResultsView;
class CatalogBrowseBarPanel;

#include "standardview.h"
#include "standardcontentview.h"

class CatalogView : public StandardContentView
{

    Q_OBJECT

public:

    CatalogView(QWidget* parent, CatalogViewModel* newViewModel);

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

    void booksCatalogViewModelChanged();
    void folderCatalogViewModelChanged();

private:

    void resizeEvent(QResizeEvent* event);

private:

    QLabel* catalogLabel;

    QScrollArea* scrollArea;
    CatalogBrowseBarPanel* browseBar;

    CatalogViewModel* viewModel;

    BookResultsCatalogView* booksView;
    CatalogResultsView* catalogView;
};

#endif // CATALOGVIEW_H
