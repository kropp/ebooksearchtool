#include "optionslistview.h"

#include <QLabel>
#include <QFile>

#include "../ViewModel/optionsviewmodel.h"

static const QString APPLY_TEXT = "Apply";

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


    applyButton = new QPushButton(this);
    applyButton->setObjectName("applyButton");
    applyButton->setText(APPLY_TEXT);
}

void OptionsListView::layoutComponents()
{
    QVBoxLayout* mainLayout = new QVBoxLayout();

    QGridLayout* optionsLayout = new QGridLayout;

    optionsLayout->setMargin(20);
    optionsLayout->setColumnMinimumWidth(0, 200);
    optionsLayout->setColumnMinimumWidth(1, 30);
    optionsLayout->setColumnStretch(0, 1);
    optionsLayout->setColumnStretch(1, 0);


    optionsLayout->addWidget(booksPerPageLabel, 0, 0, 1, 1, Qt::AlignLeft);
    optionsLayout->addWidget(booksPerPageEdit,  0, 1, 1, 1, Qt::AlignRight);

    optionsLayout->addWidget(proxyLabel, 1, 0, 1, 1, Qt::AlignLeft);
    optionsLayout->addWidget(proxyEdit,  1, 1, 1, 1, Qt::AlignRight);

    optionsLayout->addWidget(proxyPortLabel, 2, 0, 1, 1, Qt::AlignLeft);
    optionsLayout->addWidget(proxyPortEdit,  2, 1, 1, 1, Qt::AlignRight);

    mainLayout->addItem(optionsLayout);

    QHBoxLayout* applyButtonLayout = new QHBoxLayout();

    applyButtonLayout->addStretch(1);
    applyButtonLayout->addWidget(applyButton);
    applyButtonLayout->addSpacing(20);

    mainLayout->addItem(applyButtonLayout);

    this->setLayout(mainLayout);
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
    myViewModel->applyAllChanges();
}

void OptionsListView::booksCountChanged(QString newValue)
{
    booksPerPageEdit->setText(newValue);
}
