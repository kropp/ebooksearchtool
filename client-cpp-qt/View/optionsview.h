#ifndef OPTIONSVIEW_H
#define OPTIONSVIEW_H

#include "standardview.h"
#include "standardcontentview.h"
#include "selectionfilterview.h"

#include <QLabel>
#include <QHBoxLayout>
#include <QScrollArea>
#include <QResizeEvent>

class OptionsViewModel;
class OptionsListView;

class OptionsView : public StandardContentView
{

    Q_OBJECT

public:

    OptionsView(QWidget* parent, OptionsViewModel* newViewModel);

protected:

    void createComponents();
    void layoutComponents();
    void setWindowParameters();
    void setConnections();

    void resizeEvent(QResizeEvent* event);

    virtual void addItemsToLeftBarPartLayout(QHBoxLayout* leftPartLayout);
    virtual void addItemsToRightBarPartLayout(QHBoxLayout* rightPartLayout);
    virtual void addItemsToLeftContentPartLayout(QHBoxLayout* leftPartLayout);
    virtual void addItemsToRightContentPartLayout(QHBoxLayout* rightPartLayout);

private slots:

    void viewModelSearchResultsVisibilityChanged(bool availability);

private:

    QLabel* optionsLabel;

    OptionsViewModel* viewModel;
    OptionsListView* listView;

    QScrollArea* myScrollArea;

    QVBoxLayout* myCurrentVerticalLayout;
};

#endif // OPTIONSVIEW_H
