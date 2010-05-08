#include "selectionfilterview.h"

#include <QHBoxLayout>
#include <QVBoxLayout>
#include <QFile>
#include <QPalette>
#include <QAbstractItemView>


static const QString SELECTION_NONE = "None";
static const QString SELECTION_AUTHOR = "Author";
static const QString SELECTION_LANGUAGE = "Language";
static const QString SELECTION_SERVER = "Server";

SelectionFilterView::SelectionFilterView(QWidget* parent, BookResultsViewModel* bookResultViewModel)
    :StandardView(parent)
{
    myViewModel = bookResultViewModel;

    initialize();
}

void SelectionFilterView::createComponents()
{
    myFilterFrame = new QFrame(this);

    myGroupLabel = new QLabel("Group by: ", myFilterFrame);
    mySortLabel = new QLabel("Sort by: ", myFilterFrame);
    myFilterGroupLabel = new QLabel("Filter by: ", myFilterFrame);
    myFilterWordsLabel = new QLabel("Filter: ", myFilterFrame);

    myGroupCombo = new QComboBox(myFilterFrame);
    mySortCombo = new QComboBox(myFilterFrame);
    myFilterCombo = new QComboBox(myFilterFrame);

    myFilterEditBox = new QLineEdit(myFilterFrame);
    myPerformButton = new QPushButton("Filter", myFilterFrame);

    myFilterFrame->setObjectName("filterFrame");
    myPerformButton->setObjectName("filterButton");

    myFilterEditBox->setEnabled(false);

    QVector<QComboBox*> boxesVector;

    boxesVector.append(myGroupCombo);
    boxesVector.append(mySortCombo);
    boxesVector.append(myFilterCombo);

    for (int i = 0; i < boxesVector.size(); i++)
    {
        QComboBox* nextBox = boxesVector.at(i);
        nextBox->addItem(SELECTION_NONE);
        nextBox->addItem(SELECTION_AUTHOR);
        nextBox->addItem(SELECTION_LANGUAGE);
        nextBox->addItem(SELECTION_SERVER);
    }
}

void SelectionFilterView::layoutComponents()
{
    QVBoxLayout* mainLayout = new QVBoxLayout();
    mainLayout->setMargin(0);
    mainLayout->setSpacing(0);
    QVBoxLayout* filterLayout = new QVBoxLayout();
    filterLayout->setMargin(0);
    filterLayout->setSpacing(0);

    QHBoxLayout* groupLayout = new QHBoxLayout();
    groupLayout->addWidget(myGroupLabel);
    groupLayout->addWidget(myGroupCombo);

    filterLayout->addSpacing(15);
    filterLayout->addItem(groupLayout);
    filterLayout->addSpacing(10);

    QHBoxLayout* sortLayout = new QHBoxLayout();

    sortLayout->addWidget(mySortLabel);
    sortLayout->addWidget(mySortCombo);

    filterLayout->addItem(sortLayout);
    filterLayout->addSpacing(10);

    QHBoxLayout* filterGroupLayout = new QHBoxLayout();
    filterGroupLayout->addWidget(myFilterGroupLabel);
    filterGroupLayout->addWidget(myFilterCombo);

    filterLayout->addItem(filterGroupLayout);
    filterLayout->addSpacing(10);

    QHBoxLayout* filterTermLayout = new QHBoxLayout();

    filterTermLayout->addWidget(myFilterWordsLabel);
    filterTermLayout->addWidget(myFilterEditBox);
    filterTermLayout->setMargin(0);
    filterTermLayout->setSpacing(0);

    filterLayout->addItem(filterTermLayout);
    filterLayout->addSpacing(20);
    filterLayout->addWidget(myPerformButton, 0, Qt::AlignRight);
    filterLayout->addStretch(1);
    myFilterFrame->setLayout(filterLayout);
    mainLayout->addWidget(myFilterFrame);

    this->setLayout(mainLayout);
}

void SelectionFilterView::setWindowParameters()
{
    QFile styleSheetFile(":/qss/FilterStyle");
    styleSheetFile.open(QIODevice::ReadOnly);
    this->setStyleSheet(styleSheetFile.readAll());
}

void SelectionFilterView::setConnections()
{
    connect(myGroupCombo, SIGNAL(activated(QString)), this, SLOT(groupComboActivated(QString)));
    connect(mySortCombo, SIGNAL(activated(QString)), this, SLOT(sortComboActivated(QString)));
    connect(myFilterCombo, SIGNAL(activated(QString)), this, SLOT(filterComboActivated(QString)));
    connect(myFilterEditBox, SIGNAL(textChanged(QString)), this, SLOT(filterTextChanged(QString)));
    connect(myPerformButton, SIGNAL(clicked()), this, SLOT(performFilterPressed()));
}

void SelectionFilterView::groupComboActivated(const QString& string)
{
    if (!string.compare(SELECTION_NONE))
    {
        mySelectedGroupType = NONE;
    }
    else if (!string.compare(SELECTION_AUTHOR))
    {
        mySelectedGroupType = AUTHOR;
    }
    else if (!string.compare(SELECTION_LANGUAGE))
    {
        mySelectedGroupType = LANGUAGE;
    }
    else if (!string.compare(SELECTION_SERVER))
    {
        mySelectedGroupType = SERVER;
    }
}

void SelectionFilterView::sortComboActivated(const QString& string)
{
    if (!string.compare(SELECTION_NONE))
    {
        mySelectedSortType = NONE;
    }
    else if (!string.compare(SELECTION_AUTHOR))
    {
        mySelectedSortType = AUTHOR;
    }
    else if (!string.compare(SELECTION_LANGUAGE))
    {
        mySelectedSortType = LANGUAGE;
    }
    else if (!string.compare(SELECTION_SERVER))
    {
        mySelectedSortType = SERVER;
    }
}

void SelectionFilterView::filterComboActivated(const QString& string)
{
    if (!string.compare(SELECTION_NONE))
    {
        mySelectedFilterType = NONE;
        myFilterEditBox->setEnabled(false);
    }
    else
    {
        myFilterEditBox->setEnabled(true);

        if (!string.compare(SELECTION_AUTHOR))
        {
            mySelectedFilterType = AUTHOR;
        }
        else if (!string.compare(SELECTION_LANGUAGE))
        {
            mySelectedFilterType = LANGUAGE;
        }
        else if (!string.compare(SELECTION_SERVER))
        {
            mySelectedFilterType = SERVER;
        }
    }
}

void SelectionFilterView::performFilterPressed()
{
    myViewModel->changeAllFilteringDataSimultaneously(mySelectedGroupType, mySelectedSortType, mySelectedFilterType, mySelectedFilterTerm);
}

void SelectionFilterView::filterTextChanged(QString newText)
{
    mySelectedFilterTerm = newText;
}
