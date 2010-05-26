#include "optionslistview.h"
#include "serverseditview.h"

#include <QLabel>
#include <QFile>

#include "../ViewModel/optionsviewmodel.h"
#include "../ViewModel/serverseditviewmodel.h"

static const QString APPLY_TEXT = "Apply";

static const int RESIZE_MARGIN = 150;

OptionsListView::OptionsListView
(
    QWidget* parent,
    OptionsViewModel* optionsListViewModel
) : StandardView(parent)
{
    myViewModel = optionsListViewModel;
    initialize();
}

OptionsListView::~OptionsListView()
{

}

void OptionsListView::createComponents()
{
    booksPerPageLabel = new QLabel(this);
    booksPerPageLabel->setObjectName("booksPerPageLabel");
    booksPerPageLabel->setText("Books per page:");

    booksPerPageEdit = new QLineEdit(this);
    booksPerPageEdit->setObjectName("booksPerPageEdit");
    booksPerPageEdit->setText(myViewModel->getPageBooksCount());

    proxyLabel = new QLabel(this);
    proxyLabel->setObjectName("proxyLabel");
    proxyLabel->setText("Proxy:");

    proxyEdit = new QLineEdit(this);
    proxyEdit->setObjectName("proxyEdit");
    proxyEdit->setText(myViewModel->getProxy());

    proxyPortLabel = new QLabel(this);
    proxyPortLabel->setObjectName("proxyPortLabel");
    proxyPortLabel->setText("Proxy port:");

    proxyPortEdit = new QLineEdit(this);
    proxyPortEdit->setObjectName("proxyPortEdit");
    proxyPortEdit->setText(myViewModel->getProxyPort());

//    libraryPathLabel = new QLabel(this);
//    libraryPathLabel->setObjectName("libraryPathLabel");
//    libraryPathLabel->setText("Library path:");
//
//    libraryPathEdit = new QLineEdit(this);
//    libraryPathEdit->setObjectName("libraryPathEdit");
//    libraryPathEdit->setText(myViewModel->getLibraryPath());

    downloadFormatLabel = new QLabel(this);
    downloadFormatLabel->setObjectName("downloadFormatLabel");
    downloadFormatLabel->setText("Download format:");

    downloadFormatEdit = new QLineEdit(this);
    downloadFormatEdit->setObjectName("downloadFormatEdit");
    downloadFormatEdit->setText(myViewModel->getDownloadFormat());

    serversList = new ServersEditView(this, myViewModel->getServersEditVm());

    scrollArea = new QScrollArea(this);
    scrollArea->setWidget(serversList);

    applyButton = new QPushButton(this);
    applyButton->setObjectName("applyButton");
    applyButton->setText(APPLY_TEXT);
}

void OptionsListView::layoutComponents()
{
    QVBoxLayout* mainLayout = new QVBoxLayout();

    QGridLayout* optionsLayout = new QGridLayout;

    optionsLayout->setMargin(20);
    optionsLayout->setSpacing(20);
    optionsLayout->setColumnMinimumWidth(0, 200);
    optionsLayout->setColumnMinimumWidth(1, 30);
    optionsLayout->setColumnStretch(0, 1);
    optionsLayout->setColumnStretch(1, 0);


    optionsLayout->addWidget(booksPerPageLabel, 0, 0, 1, 1);
    optionsLayout->addWidget(booksPerPageEdit,  0, 1, 1, 1);

    optionsLayout->addWidget(proxyLabel, 1, 0, 1, 1);
    optionsLayout->addWidget(proxyEdit,  1, 1, 1, 1);

    optionsLayout->addWidget(proxyPortLabel, 2, 0, 1, 1);
    optionsLayout->addWidget(proxyPortEdit,  2, 1, 1, 1);

//    optionsLayout->addWidget(libraryPathLabel, 3, 0, 1, 1);
//    optionsLayout->addWidget(libraryPathEdit,  3, 1, 1, 1);

    optionsLayout->addWidget(downloadFormatLabel, 4, 0, 1, 1);
    optionsLayout->addWidget(downloadFormatEdit,  4, 1, 1, 1);

    optionsLayout->addWidget(scrollArea, 5, 0, 1, 2);

    mainLayout->addItem(optionsLayout);
    mainLayout->addStretch(1);

    QHBoxLayout* applyButtonLayout = new QHBoxLayout();

    applyButtonLayout->addStretch(1);
    applyButtonLayout->addWidget(applyButton);
    applyButtonLayout->addSpacing(20);

    mainLayout->addItem(applyButtonLayout);

    this->setLayout(mainLayout);
}

void OptionsListView::resizeEvent(QResizeEvent* event)
{
    int newWidth = event->size().width();
    serversList->setFixedWidth(newWidth - RESIZE_MARGIN);
}

void OptionsListView::setWindowParameters()
{
    QFile styleSheetFile(":/qss/OptionsListStyle");
    styleSheetFile.open(QIODevice::ReadOnly);
    this->setStyleSheet(styleSheetFile.readAll());
}

void OptionsListView::setConnections()
{
    connect(applyButton, SIGNAL(clicked()), this, SLOT(applyAllChanges()));
    connect(myViewModel, SIGNAL(bookCountChanged(QString)), this, SLOT(booksCountChanged(QString)));
}

void OptionsListView::applyAllChanges()
{
    myViewModel->requestToChangePageBooksCount(booksPerPageEdit->text());
    myViewModel->requestToChangeProxy(proxyEdit->text());
    myViewModel->requestToChangeProxyPort(proxyPortEdit->text());
    //myViewModel->requestToChangeLibraryPath(libraryPathEdit->text());
    myViewModel->requestToChangeDownloadFormat(downloadFormatEdit->text());
    myViewModel->applyAllChanges();
}

void OptionsListView::booksCountChanged(QString newValue)
{
    booksPerPageEdit->setText(newValue);
}
