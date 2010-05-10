#include "pageview.h"

#include <QHBoxLayout>
#include <QFile>

#include "../ViewModel/bookresultsviewmodel.h"

static const int MAX_PAGES_COUNT = 200;

PageView::PageView(QWidget* parent, BookResultsViewModel* resultsModel) : StandardView(parent)
{
    viewModel = resultsModel;
    currentPage = 0;
    initialize();

    pagesCountChanged(0, 0);
}

void PageView::createComponents()
{
    pageButtons = QVector<MultiStateButton*>();
    pageLabel = new QLabel("Page:", this);
    pageLabel->setMargin(0);

    for (int i = 0; i < MAX_PAGES_COUNT; i++)
    {
        MultiStateButton* nextButton = new MultiStateButton(this);
        nextButton->setText(QString::number(i + 1));
        pageButtons.append(nextButton);
    }

    highlightSelectedPageButton();

    prevButton = new QPushButton(this);
    nextButton = new QPushButton(this);

    prevButton->setObjectName("prevButton");
    nextButton->setObjectName("nextButton");

    nextButton->hide();
    prevButton->hide();
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



    pagesLayout->addWidget(pageLabel, 0, 0, 1, 1, Qt::AlignVCenter);

    pagesLayout->setMargin(0);
    pagesLayout->setSpacing(0);

    QHBoxLayout* prevLayout = new QHBoxLayout();

    prevLayout->addWidget(prevButton, 0, Qt::AlignVCenter);
    prevLayout->addStretch(1);

    pagesLayout->addItem(prevLayout, 0, 1);

    QHBoxLayout* pagesArrayLayout = new QHBoxLayout();

    for (int i = 0; i < pageButtons.size(); i++)
    {
        QPushButton* nextButton = pageButtons.at(i);
        pagesArrayLayout->addWidget(nextButton);
    }

    pagesLayout->addItem(pagesArrayLayout, 0, 2);

    QHBoxLayout* nextLayout = new QHBoxLayout();
    nextLayout->addWidget(nextButton, 0, Qt::AlignVCenter);
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
    connect(viewModel, SIGNAL(pagesCountChanged(int, int)), this, SLOT(pagesCountChanged(int, int)));
    connect(viewModel, SIGNAL(pagePrevAvailabilityChanged(bool)), this, SLOT(pagePrevAvailabilityChanged(bool)));
    connect(viewModel, SIGNAL(pageNextAvailabilityChanged(bool)), this, SLOT(pageNextAvailabilityChanged(bool)));
    connect(prevButton, SIGNAL(clicked()), viewModel, SLOT(requestPrevPagesWindow()));
    connect(nextButton, SIGNAL(clicked()), viewModel, SLOT(requestNextPagesWindow()));
    connect(viewModel, SIGNAL(pageChanged(int)), this, SLOT(pageChanged(int)));

    for (int i = 0; i < pageButtons.size(); i++)
    {
        QPushButton* nextButton = pageButtons.at(i);

        connect(nextButton, SIGNAL(clicked()), this, SLOT(pageClicked()));
    }
}

void PageView::pageChanged(int page)
{
    currentPage = page;
    highlightSelectedPageButton();
}

void PageView::pageClicked()
{
    MultiStateButton* senderButton = (MultiStateButton*)sender();

    int foundIndex = -1;

    for (int i = 0; i < pageButtons.size(); i++)
    {
        MultiStateButton* nextButton = pageButtons.at(i);

        if (senderButton == nextButton)
        {
            foundIndex = i;
            break;
        }
    }

    currentPage = foundIndex;

    viewModel->requestToChangePage(currentPage);
}

void PageView::highlightSelectedPageButton()
{
    for (int i = 0; i < pageButtons.size(); i++)
    {
        MultiStateButton* nextButton = pageButtons.at(i);
        nextButton->setState("normal");
    }

    if (currentPage < pageButtons.size())
    {
        MultiStateButton* selectedButton = pageButtons.at(currentPage);
        selectedButton->setState("selected");
    }

    this->setStyleSheet(this->styleSheet());
}

void PageView::pagePrevAvailabilityChanged(bool availability)
{
    if (availability)
    {
        prevButton->show();
    }
    else
    {
        prevButton->hide();
    }
}

void PageView::pageNextAvailabilityChanged(bool availability)
{
    if (availability)
    {
        nextButton->show();
    }
    else
    {
        nextButton->hide();
    }
}

void PageView::hideButtons()
{
    for (int i = 0; i < pageButtons.size(); i++)
    {
        QPushButton* nextButton = pageButtons.at(i);
        nextButton->hide();
    }
}

void PageView::pagesCountChanged(int newPagesCount, int startingPage)
{
    hideButtons();

    int maxPagesCount = newPagesCount < pageButtons.size() ? newPagesCount : pageButtons.size();

    for (int i = 0; i < maxPagesCount; i++)
    {
        QPushButton* nextButton = pageButtons.at(i);
        nextButton->setText(QString::number(1 + i + startingPage));
        nextButton->show();
    }

    highlightSelectedPageButton();
}
