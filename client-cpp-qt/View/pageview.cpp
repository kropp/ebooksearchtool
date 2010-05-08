#include "pageview.h"

#include <QHBoxLayout>
#include <QFile>

#include "../ViewModel/bookresultsviewmodel.h"

static const int MAX_PAGES_COUNT = 200;

PageView::PageView(QWidget* parent, BookResultsViewModel* resultsModel) : StandardView(parent)
{
    myViewModel = resultsModel;
    myCurrentPage = 0;
    initialize();

    pagesCountChanged(0, 0);
}

void PageView::createComponents()
{
    myPageButtons = QVector<MultiStateButton*>();
    myPageLabel = new QLabel("Page:", this);
    myPageLabel->setMargin(0);

    for (int i = 0; i < MAX_PAGES_COUNT; i++)
    {
        MultiStateButton* nextButton = new MultiStateButton(this);
        nextButton->setText(QString::number(i + 1));
        myPageButtons.append(nextButton);
    }

    highlightSelectedPageButton();

    myPrevButton = new QPushButton(this);
    myNextButton = new QPushButton(this);

    myPrevButton->setObjectName("prevButton");
    myNextButton->setObjectName("nextButton");

    myNextButton->hide();
    myPrevButton->hide();
}

void PageView::layoutComponents()
{
    QGridLayout *pagesLayout = new QGridLayout;

    pagesLayout->setColumnMinimumWidth(0, 50);
    pagesLayout->setColumnMinimumWidth(1, 30);
    pagesLayout->setColumnStretch(0, 0);
    pagesLayout->setColumnStretch(1, 0);
    pagesLayout->setColumnStretch(2, 1);
    pagesLayout->setColumnStretch(3, 0);



    pagesLayout->addWidget(myPageLabel, 0, 0, 1, 1, Qt::AlignVCenter);

    pagesLayout->setMargin(0);
    pagesLayout->setSpacing(0);

    QHBoxLayout* prevLayout = new QHBoxLayout();

    prevLayout->addWidget(myPrevButton, 0, Qt::AlignVCenter);
    prevLayout->addStretch(1);

    pagesLayout->addItem(prevLayout, 0, 1);

    QHBoxLayout* pagesArrayLayout = new QHBoxLayout();

    for (int i = 0; i < myPageButtons.size(); i++)
    {
        QPushButton* nextButton = myPageButtons.at(i);
        pagesArrayLayout->addWidget(nextButton);
    }

    pagesLayout->addItem(pagesArrayLayout, 0, 2);

    QHBoxLayout* nextLayout = new QHBoxLayout();
    nextLayout->addWidget(myNextButton, 0, Qt::AlignVCenter);
    nextLayout->addStretch(1);

    pagesLayout->addItem(nextLayout, 0, 3);

    this->setLayout(pagesLayout);
    this->update();
}

void PageView::setWindowParameters()
{
    QFile styleSheetFile(":/qss/PageViewStyle");
    styleSheetFile.open(QIODevice::ReadOnly);
    this->setStyleSheet(styleSheetFile.readAll());
}

void PageView::setConnections()
{
    connect(myViewModel, SIGNAL(pagesCountChanged(int, int)), this, SLOT(pagesCountChanged(int, int)));
    connect(myViewModel, SIGNAL(pagePrevAvailabilityChanged(bool)), this, SLOT(pagePrevAvailabilityChanged(bool)));
    connect(myViewModel, SIGNAL(pageNextAvailabilityChanged(bool)), this, SLOT(pageNextAvailabilityChanged(bool)));
    connect(myPrevButton, SIGNAL(clicked()), myViewModel, SLOT(requestPrevPagesWindow()));
    connect(myNextButton, SIGNAL(clicked()), myViewModel, SLOT(requestNextPagesWindow()));
    connect(myViewModel, SIGNAL(pageChanged(int)), this, SLOT(pageChanged(int)));

    for (int i = 0; i < myPageButtons.size(); i++)
    {
        QPushButton* nextButton = myPageButtons.at(i);

        connect(nextButton, SIGNAL(clicked()), this, SLOT(pageClicked()));
    }
}

void PageView::pageChanged(int page)
{
    myCurrentPage = page;
    highlightSelectedPageButton();
}

void PageView::pageClicked()
{
    MultiStateButton* senderButton = (MultiStateButton*)sender();

    int foundIndex = -1;

    for (int i = 0; i < myPageButtons.size(); i++)
    {
        MultiStateButton* nextButton = myPageButtons.at(i);

        if (senderButton == nextButton)
        {
            foundIndex = i;
            break;
        }
    }

    myCurrentPage = foundIndex;

    myViewModel->requestToChangePage(myCurrentPage);
}

void PageView::highlightSelectedPageButton()
{
    for (int i = 0; i < myPageButtons.size(); i++)
    {
        MultiStateButton* nextButton = myPageButtons.at(i);
        nextButton->setState("normal");
    }

    if (myCurrentPage < myPageButtons.size())
    {
        MultiStateButton* selectedButton = myPageButtons.at(myCurrentPage);
        selectedButton->setState("selected");
    }

    this->setStyleSheet(this->styleSheet());
}

void PageView::pagePrevAvailabilityChanged(bool availability)
{
    if (availability)
    {
        myPrevButton->show();
    }
    else
    {
        myPrevButton->hide();
    }
}

void PageView::pageNextAvailabilityChanged(bool availability)
{
    if (availability)
    {
        myNextButton->show();
    }
    else
    {
        myNextButton->hide();
    }
}

void PageView::hideButtons()
{
    for (int i = 0; i < myPageButtons.size(); i++)
    {
        QPushButton* nextButton = myPageButtons.at(i);
        nextButton->hide();
    }
}

void PageView::pagesCountChanged(int newPagesCount, int startingPage)
{
    hideButtons();

    int maxPagesCount = newPagesCount < myPageButtons.size() ? newPagesCount : myPageButtons.size();

    for (int i = 0; i < maxPagesCount; i++)
    {
        QPushButton* nextButton = myPageButtons.at(i);
        nextButton->setText(QString::number(1 + i + startingPage));
        nextButton->show();
    }

    highlightSelectedPageButton();
}
