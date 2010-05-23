#include "optionsview.h"
#include "optionslistview.h"

#include "../ViewModel/optionsviewmodel.h"

#include <QVBoxLayout>
#include <QIcon>
#include <QFile>

static const int RESIZE_MARGIN = 310;

OptionsView::OptionsView(QWidget* parent, OptionsViewModel* newViewModel) : StandardContentView(parent)
{
    viewModel = newViewModel;

    initialize();
}

void OptionsView::createComponents()
{
    StandardContentView::createComponents();

    optionsLabel = new QLabel(this);
    optionsLabel->setObjectName("optionsLabel");

    listView = new OptionsListView(this, viewModel);

    myScrollArea = new QScrollArea(this);

    myScrollArea->setWidget(listView);
    myScrollArea->setObjectName("scrollArea");


}

void OptionsView::layoutComponents()
{
    StandardContentView::layoutComponents();
}

void OptionsView::viewModelSearchResultsVisibilityChanged(bool /*visibility*/)
{

}

void OptionsView::setWindowParameters()
{
    StandardContentView::setWindowParameters();

    QFile styleSheetFile(":/qss/OptionsStyle");
    styleSheetFile.open(QIODevice::ReadOnly);
    this->setStyleSheet(this->styleSheet() + styleSheetFile.readAll());
}

void OptionsView::resizeEvent(QResizeEvent* event)
{
    int newWidth = event->size().width();
    listView->setFixedWidth(newWidth - RESIZE_MARGIN);
}

void OptionsView::setConnections()
{
    connect(viewModel, SIGNAL(libraryResultsAvailabilityChanged(bool)), this, SLOT(viewModelSearchResultsVisibilityChanged(bool)));
}

void OptionsView::addItemsToLeftBarPartLayout(QHBoxLayout* leftPartLayout)
{
    leftPartLayout->addWidget(optionsLabel);
}

void OptionsView::addItemsToRightBarPartLayout(QHBoxLayout* rightPartLayout)
{

}


void OptionsView::addItemsToLeftContentPartLayout(QHBoxLayout* leftPartLayout)
{

}

void OptionsView::addItemsToRightContentPartLayout(QHBoxLayout* rightPartLayout)
{

    QVBoxLayout* mainLayout = new QVBoxLayout();

    mainLayout->setMargin(0);
    mainLayout->setSpacing(0);
    mainLayout->addWidget(myScrollArea);
    //mainLayout->addWidget(listView);

    rightPartLayout->addItem(mainLayout);
}

