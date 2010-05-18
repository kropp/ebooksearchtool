#include "mainview.h"
#include "programmodeselectorview.h"
#include "../ViewModel/programmodeviewmodel.h"
#include "../Model/librarymanager.h"


#include <QHBoxLayout>
#include <QFile>
#include <QCloseEvent>

MainView::MainView() : StandardView(0) { initialize();}

void MainView::createComponents()
{
    programSelectorView = new ProgramModeSelectorView(this, new ProgramModeViewModel);
}

void MainView::layoutComponents()
{
    QHBoxLayout* mainLayout = new QHBoxLayout;

    mainLayout->setSpacing(0);
    mainLayout->setMargin(0);
    mainLayout->addWidget(programSelectorView);

    this->setLayout(mainLayout);
}

void MainView::setWindowParameters()
{
    QFile styleSheetFile(":/qss/Common");
    styleSheetFile.open(QIODevice::ReadOnly);
    this->setStyleSheet(styleSheetFile.readAll());
}

void MainView::closeEvent (QCloseEvent * e ) {
    LibraryManager::getInstance()->saveLibrary();
    e->accept();
}
