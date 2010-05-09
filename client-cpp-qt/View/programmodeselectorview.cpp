#include "programmodeselectorview.h"

#include <QHBoxLayout>
#include <QSplitter>
#include <QLineEdit>
#include <QFile>

#include "../ViewModel/programmodeviewmodel.h"

#include "searchview.h"

static const QString SEARCH_MODE_ICON = ":SearchMode";
static const QString LIBRARY_MODE_ICON = ":LibraryMode";
static const QString CATALOG_MODE_ICON = ":CatalogMode";

ProgramModeSelectorView::ProgramModeSelectorView(QWidget* parent,ProgramModeViewModel* resultsModel) : StandardView(parent)
{
    myViewModel = resultsModel;
    initialize();

    hideAllModeChildren();
    myViewModel->requestToChangeProgramMode(SEARCH);
}

void ProgramModeSelectorView::createComponents()
{
    mySearchView = new SearchView(this, myViewModel->getSearchViewModel());

    myHeaderLeftImage = new QLabel(this);
    myHeaderStretchImage = new QLabel(this);
    myHeaderRightImage = new QLabel(this);

    myHeaderLeftImage->setObjectName("headerLeftImage");
    myHeaderStretchImage->setObjectName("headerStretchImage");
    myHeaderRightImage->setObjectName("headerRightImage");

    mySearchModeButton = new MultiStateButton(this);

    mySearchModeButton->setObjectName("searchModeButton");
}

void ProgramModeSelectorView::layoutComponents()
{
    QVBoxLayout* layout = new QVBoxLayout();
    QHBoxLayout* headerLayout = new QHBoxLayout();

    headerLayout->setMargin(0);
    headerLayout->setSpacing(0);
    headerLayout->addWidget(myHeaderLeftImage);

    QHBoxLayout* rightHeaderPartLayout = new QHBoxLayout();

    rightHeaderPartLayout->addSpacing(15);
    rightHeaderPartLayout->addWidget(mySearchModeButton, 0, Qt::AlignBottom | Qt::AlignLeft);

    myHeaderRightImage->setLayout(rightHeaderPartLayout);

    headerLayout->addWidget(myHeaderRightImage);
    headerLayout->addWidget(myHeaderStretchImage);

    layout->addItem(headerLayout);
    layout->addWidget(mySearchView);
    layout->setMargin(0);
    layout->setSpacing(0);

    this->setLayout(layout);
}

void ProgramModeSelectorView::hideAllModeChildren()
{
    mySearchView->hide();
}

void ProgramModeSelectorView::grayAllButtons()
{
    mySearchModeButton->setState("grayed");
}

void ProgramModeSelectorView::enableSearchButton()
{
    mySearchModeButton->setState("normal");
}

void ProgramModeSelectorView::modeButtonPressed()
{
    QPushButton* senderButton = (QPushButton*)sender();

    if (senderButton == mySearchModeButton)
    {
        myViewModel->requestToChangeProgramMode(SEARCH);
    }
}

void ProgramModeSelectorView::setWindowParameters()
{
    QFile styleSheetFile(":/qss/SelectorStyle");
    styleSheetFile.open(QIODevice::ReadOnly);
    this->setStyleSheet(styleSheetFile.readAll());
}

void ProgramModeSelectorView::setConnections()
{
    connect(myViewModel, SIGNAL(viewModeChanged(ProgramMode)), this, SLOT(viewModeChanged(ProgramMode)));
    connect(mySearchModeButton, SIGNAL(clicked()), this, SLOT(modeButtonPressed()));
}

void ProgramModeSelectorView::viewModeChanged(ProgramMode newMode)
{
    hideAllModeChildren();
    grayAllButtons();

    switch(newMode)
    {
    case SEARCH:
        mySearchView->show();
        enableSearchButton();
        break;
    }

    mySearchModeButton->setStyleSheet(this->styleSheet());
}
