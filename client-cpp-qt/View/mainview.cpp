#include "mainview.h"
#include "programmodeselectorview.h"
#include "../ViewModel/programmodeviewmodel.h"

#include <QHBoxLayout>
#include <QFile>

MainView::MainView() : StandardView(0) { initialize();}

void MainView::createComponents()
{
    myProgramSelectorView = new ProgramModeSelectorView(this, new ProgramModeViewModel);
}

void MainView::layoutComponents()
{
    QHBoxLayout* mainLayout = new QHBoxLayout;

    mainLayout->setSpacing(0);
    mainLayout->setMargin(0);
    mainLayout->addWidget(myProgramSelectorView);

    this->setLayout(mainLayout);
}

void MainView::setWindowParameters()
{
    QFile styleSheetFile(":/qss/Common");
    styleSheetFile.open(QIODevice::ReadOnly);
    this->setStyleSheet(styleSheetFile.readAll());
}
