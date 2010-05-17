#include "catalogbrowsebarpanel.h"

#include "../ViewModel/catalogbrowsebarviewmodel.h"

#include <QHBoxLayout>
#include <QIcon>
#include <QFile>

CatalogBrowseBarPanel::CatalogBrowseBarPanel(QWidget* parent, CatalogBrowseBarViewModel* newViewModel) : StandardView(parent)
{
    myViewModel = newViewModel;
    initialize();
}

void CatalogBrowseBarPanel::createComponents()
{
    myBarFrame = new QFrame(this);
    myBarFrame->setObjectName("barFrame");

    myHomeButton = new QPushButton(this);
    myGoUpButton = new QPushButton(this);
    myGoBackButton = new QPushButton(this);
    myGoForwardButton = new QPushButton(this);

    myProgressBar = new QProgressBar(this);

    myHomeButton->setObjectName("homeButton");
    myGoUpButton->setObjectName("goUpButton");
    myGoBackButton->setObjectName("goBackButton");
    myGoForwardButton->setObjectName("goForwardButton");
    myProgressBar->setObjectName("progressBar");

    myGoUpButton->setEnabled(myViewModel->getGoUpAvailability());
    myGoBackButton->setEnabled(myViewModel->getGoBackAvailability());
    myGoForwardButton->setEnabled(myViewModel->getGoForwardAvailability());
    myProgressBar->setMaximum(0);
    myProgressBar->setMinimum(0);

    myProgressBar->hide();
}

void CatalogBrowseBarPanel::setUpAvailability(bool newValue)
{
    myGoUpButton->setEnabled(newValue);
}

void CatalogBrowseBarPanel::setBackAvailability(bool newValue)
{
    myGoBackButton->setEnabled(newValue);
}

void CatalogBrowseBarPanel::setForwardAvailability(bool newValue)
{
    myGoForwardButton->setEnabled(newValue);
}

void CatalogBrowseBarPanel::showProgress() {
        myProgressBar->show();
}

void CatalogBrowseBarPanel::hideProgress() {
        myProgressBar->hide();
}

void CatalogBrowseBarPanel::layoutComponents()
{
    QHBoxLayout* headerLayout = new QHBoxLayout();
    headerLayout->setMargin(0);
    headerLayout->setSpacing(0);

    QHBoxLayout* frameLayout = new QHBoxLayout();
    frameLayout->setMargin(0);
    frameLayout->setSpacing(0);

    frameLayout->addSpacing(20);
    frameLayout->addWidget(myHomeButton, 0, Qt::AlignVCenter);
    frameLayout->addWidget(myGoUpButton, 0, Qt::AlignVCenter);
    frameLayout->addWidget(myGoBackButton, 0, Qt::AlignVCenter);
    frameLayout->addWidget(myGoForwardButton, 0, Qt::AlignVCenter);
    frameLayout->addWidget(myProgressBar, 0, Qt::AlignRight);
    frameLayout->addStretch(1);

    myBarFrame->setLayout(frameLayout);

    headerLayout->addWidget(myBarFrame);
    this->setLayout(headerLayout);
}

void CatalogBrowseBarPanel::setWindowParameters()
{
    QFile styleSheetFile(":/qss/BrowseBarStyle");
    styleSheetFile.open(QIODevice::ReadOnly);
    this->setStyleSheet(this->styleSheet() + styleSheetFile.readAll());
}

void CatalogBrowseBarPanel::setConnections()
{
    connect(myHomeButton, SIGNAL(clicked()), myViewModel, SLOT(goHome()));
    connect(myGoBackButton, SIGNAL(clicked()), myViewModel, SLOT(goBack()));
    connect(myGoForwardButton, SIGNAL(clicked()), myViewModel, SLOT(goForward()));
    connect(myGoUpButton, SIGNAL(clicked()), myViewModel, SLOT(goUp()));
    connect(myViewModel, SIGNAL(upAvailabilityChanged(bool)), this, SLOT(setUpAvailability(bool)));
    connect(myViewModel, SIGNAL(backAvailabilityChanged(bool)), this, SLOT(setBackAvailability(bool)));
    connect(myViewModel, SIGNAL(forwardAvailabilityChanged(bool)), this, SLOT(setForwardAvailability(bool)));
}
