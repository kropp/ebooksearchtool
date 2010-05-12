#include "programmodeselectorview.h"

#include <QHBoxLayout>
#include <QSplitter>
#include <QLineEdit>
#include <QFile>

#include "../ViewModel/programmodeviewmodel.h"

#include "searchview.h"
#include "libraryview.h"
#include "catalogview.h"

static const QString SEARCH_MODE_ICON = ":SearchMode";
static const QString LIBRARY_MODE_ICON = ":LibraryMode";
static const QString CATALOG_MODE_ICON = ":CatalogMode";

ProgramModeSelectorView::ProgramModeSelectorView(QWidget* parent,ProgramModeViewModel* resultsModel) : StandardView(parent)
{
    viewModel = resultsModel;
    initialize();

    hideAllModeChildren();
    viewModel->requestToChangeProgramMode(SEARCH);
}

void ProgramModeSelectorView::createComponents()
{
    searchView = new SearchView(this, viewModel->getSearchViewModel());
    libraryView = new LibraryView(this, viewModel->getLibraryViewModel());
    catalogView = new CatalogView(this, viewModel->getCatalogViewModel());

    headerLeftImage = new QLabel(this);
    headerStretchImage = new QLabel(this);
    headerRightImage = new QLabel(this);

    headerLeftImage->setObjectName("headerLeftImage");
    headerStretchImage->setObjectName("headerStretchImage");
    headerRightImage->setObjectName("headerRightImage");

    searchModeButton = new MultiStateButton(this);
  //  libraryModeButton = new MultiStateButton(this);
    catalogModeButton = new MultiStateButton(this);

    searchModeButton->setObjectName("searchModeButton");
   // libraryModeButton->setObjectName("libraryModeButton");
    catalogModeButton->setObjectName("catalogModeButton");
}

void ProgramModeSelectorView::layoutComponents()
{
    QVBoxLayout* layout = new QVBoxLayout();
    QHBoxLayout* headerLayout = new QHBoxLayout();

    headerLayout->setMargin(0);
    headerLayout->setSpacing(0);
    headerLayout->addWidget(headerLeftImage);

    QHBoxLayout* rightHeaderPartLayout = new QHBoxLayout();

    rightHeaderPartLayout->addSpacing(15);
    rightHeaderPartLayout->addWidget(searchModeButton, 0, Qt::AlignBottom | Qt::AlignLeft);
   // rightHeaderPartLayout->addWidget(libraryModeButton, 0, Qt::AlignBottom | Qt::AlignLeft);
    rightHeaderPartLayout->addWidget(catalogModeButton, 0, Qt::AlignBottom | Qt::AlignLeft);

    headerRightImage->setLayout(rightHeaderPartLayout);

    headerLayout->addWidget(headerRightImage);
    headerLayout->addWidget(headerStretchImage);

    layout->addItem(headerLayout);
    layout->addWidget(searchView);
    layout->addWidget(libraryView);
    layout->addWidget(catalogView);
    layout->setMargin(0);
    layout->setSpacing(0);

    this->setLayout(layout);
}

void ProgramModeSelectorView::hideAllModeChildren()
{
    searchView->hide();
    libraryView->hide();
    catalogView->hide();
}

void ProgramModeSelectorView::grayAllButtons()
{
    searchModeButton->setState("grayed");
  //  libraryModeButton->setState("grayed");
    catalogModeButton->setState("grayed");
}

void ProgramModeSelectorView::enableSearchButton()
{
    searchModeButton->setState("normal");
}

void ProgramModeSelectorView::enableLibraryButton()
{
    libraryModeButton->setProperty("state", "normal");
}

void ProgramModeSelectorView::enableCatalogButton()
{
    catalogModeButton->setState("normal");
}

void ProgramModeSelectorView::modeButtonPressed()
{
    QPushButton* senderButton = (QPushButton*)sender();

    if (senderButton == searchModeButton)
    {
        viewModel->requestToChangeProgramMode(SEARCH);
    }
    else if (senderButton == libraryModeButton)
    {
        viewModel->requestToChangeProgramMode(LIBRARY);
    }
    else if (senderButton == catalogModeButton)
    {
        viewModel->requestToChangeProgramMode(CATALOG);
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
    connect(viewModel, SIGNAL(viewModeChanged(ProgramMode)), this, SLOT(viewModeChanged(ProgramMode)));
    connect(searchModeButton, SIGNAL(clicked()), this, SLOT(modeButtonPressed()));
  //  connect(libraryModeButton, SIGNAL(clicked()), this, SLOT(modeButtonPressed()));
    connect(catalogModeButton, SIGNAL(clicked()), this, SLOT(modeButtonPressed()));
}

void ProgramModeSelectorView::viewModeChanged(ProgramMode newMode)
{
    hideAllModeChildren();
    grayAllButtons();

    switch(newMode)
    {
    case SEARCH:
        searchView->show();
        enableSearchButton();
        break;
    case LIBRARY:
    /*    libraryView->show();
        enableLibraryButton();
       */ break;
    case CATALOG:
        catalogView->show();
        enableCatalogButton();
        break;
    }

    searchModeButton->setStyleSheet(this->styleSheet());
   // libraryModeButton->setStyleSheet(this->styleSheet());
    catalogModeButton->setStyleSheet(this->styleSheet());

}
