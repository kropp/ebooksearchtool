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
    viewModel = bookResultViewModel;

    initialize();
}

void SelectionFilterView::createComponents()
{
    filterFrame = new QFrame(this);

    groupLabel = new QLabel("Group by: ", filterFrame);
    sortLabel = new QLabel("Sort by: ", filterFrame);
    filterGroupLabel = new QLabel("Filter by: ", filterFrame);
    filterWordsLabel = new QLabel("Filter words: ", filterFrame);

    groupCombo = new QComboBox(filterFrame);
    sortCombo = new QComboBox(filterFrame);
    filterCombo = new QComboBox(filterFrame);

    filterEditBox = new QLineEdit(filterFrame);
    filterEditBox->hide();
    filterWordsLabel->hide();
    performButton = new QPushButton("Filter", filterFrame);

    performButton->setCursor(Qt::PointingHandCursor);

    filterFrame->setObjectName("filterFrame");
    performButton->setObjectName("filterButton");

    filterEditBox->setEnabled(true);

    QVector<QComboBox*> boxesVector;

    boxesVector.append(groupCombo);
    boxesVector.append(sortCombo);
    boxesVector.append(filterCombo);

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
    groupLayout->addWidget(groupLabel);
    groupLayout->addWidget(groupCombo);

    filterLayout->addSpacing(15);
    filterLayout->addItem(groupLayout);
    filterLayout->addSpacing(10);

    QHBoxLayout* sortLayout = new QHBoxLayout();

    sortLayout->addWidget(sortLabel);
    sortLayout->addWidget(sortCombo);

    filterLayout->addItem(sortLayout);
    filterLayout->addSpacing(10);

    QHBoxLayout* filterGroupLayout = new QHBoxLayout();
    filterGroupLayout->addWidget(filterGroupLabel);
    filterGroupLayout->addWidget(filterCombo);

    filterLayout->addItem(filterGroupLayout);
    filterLayout->addSpacing(10);

    QHBoxLayout* filterTermLayout = new QHBoxLayout();

    filterTermLayout->addWidget(filterWordsLabel);
    filterTermLayout->addWidget(filterEditBox);
    filterTermLayout->setMargin(0);
    filterTermLayout->setSpacing(0);

    filterLayout->addItem(filterTermLayout);
    filterLayout->addSpacing(20);
    filterLayout->addWidget(performButton, 0, Qt::AlignRight);
    filterLayout->addStretch(1);
    filterFrame->setLayout(filterLayout);
    mainLayout->addWidget(filterFrame);

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
    connect(groupCombo, SIGNAL(activated(QString)), this, SLOT(groupComboActivated(QString)));
    connect(sortCombo, SIGNAL(activated(QString)), this, SLOT(sortComboActivated(QString)));
    connect(filterCombo, SIGNAL(activated(QString)), this, SLOT(filterComboActivated(QString)));
    connect(filterEditBox, SIGNAL(textChanged(QString)), this, SLOT(filterTextChanged(QString)));
    connect(performButton, SIGNAL(clicked()), this, SLOT(performFilterPressed()));
}

void SelectionFilterView::groupComboActivated(const QString& string)
{
    if (!string.compare(SELECTION_NONE))
    {
        selectedGroupType = NONE;
    }
    else if (!string.compare(SELECTION_AUTHOR))
    {
        selectedGroupType = AUTHOR;
    }
    else if (!string.compare(SELECTION_LANGUAGE))
    {
        selectedGroupType = LANGUAGE;
    }
    else if (!string.compare(SELECTION_SERVER))
    {
        selectedGroupType = SERVER;
    }
}

void SelectionFilterView::sortComboActivated(const QString& string)
{
    if (!string.compare(SELECTION_NONE))
    {
        selectedSortType = NONE;
    }
    else if (!string.compare(SELECTION_AUTHOR))
    {
        selectedSortType = AUTHOR;
    }
    else if (!string.compare(SELECTION_LANGUAGE))
    {
        selectedSortType = LANGUAGE;
    }
    else if (!string.compare(SELECTION_SERVER))
    {
        selectedSortType = SERVER;
    }
}

void SelectionFilterView::filterComboActivated(const QString& string)
{
    if (!string.compare(SELECTION_NONE))
    {
        selectedFilterType = NONE;
        filterEditBox->hide();
        filterWordsLabel->hide();
    }
    else
    {
        filterEditBox->show();
        filterWordsLabel->show();

        if (!string.compare(SELECTION_AUTHOR))
        {
            selectedFilterType = AUTHOR;
        }
        else if (!string.compare(SELECTION_LANGUAGE))
        {
            selectedFilterType = LANGUAGE;
        }
        else if (!string.compare(SELECTION_SERVER))
        {
            selectedFilterType = SERVER;
        }
    }
}

void SelectionFilterView::performFilterPressed()
{
    viewModel->changeAllFilteringDataSimultaneously(selectedGroupType, selectedSortType, selectedFilterType, selectedFilterTerm);
}

void SelectionFilterView::filterTextChanged(QString newText)
{
    selectedFilterTerm = newText;
}
