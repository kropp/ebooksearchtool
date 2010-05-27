#include "serverview.h"
#include "../ViewModel/servervm.h"

#include <QVBoxLayout>
#include <QHBoxLayout>
#include <QGridLayout>
#include <QFile>

static const QString SERVER_PATH_LABEL = "host:";
static const QString SERVER_SEARCH_PATH_LABEL = "Book search url:";
static const QString SERVER_ATOM_PATH_LABEL = "Root OPDS catalog url:";

ServerView::ServerView(QWidget* parent, ServerViewModel* resultViewModel)
    :StandardView(parent)
{
    myViewModel = resultViewModel;
    initialize();
}

ServerView::~ServerView()
{

}

void ServerView::createComponents()
{
    serverAliasLabel = new QLabel(this);
    serverPathLabel = new QLabel(this);
    serverSearchPathLabel = new QLabel(this);
    serverAtomPathLabel = new QLabel(this);

    serverAliasLabel->setText(myViewModel->getServerAlias());
    serverPathLabel->setText(SERVER_PATH_LABEL);
    serverSearchPathLabel->setText(SERVER_SEARCH_PATH_LABEL);
    serverAtomPathLabel->setText(SERVER_ATOM_PATH_LABEL);

    serverAliasLabel->setObjectName("serverAliasLabel");
    serverPathLabel->setObjectName("serverPathLabel");
    serverSearchPathLabel->setObjectName("serverSearchPathLabel");
    serverAtomPathLabel->setObjectName("serverAtomPathLabel");

    deleteButton = new QPushButton(this);
    deleteButton->setObjectName("deleteServer");

    serverPathEdit = new QLineEdit(this);
    searchPathEdit = new QLineEdit(this);
    atomPathEdit = new QLineEdit(this);

    serverPathEdit->setText(myViewModel->getServerPath());
    searchPathEdit->setText(myViewModel->getServerSearchPath());
    atomPathEdit->setText(myViewModel->getServerAtomPath());

    serverPathEdit->setObjectName("serverPathEdit");
    searchPathEdit->setObjectName("searchPathEdit");
    atomPathEdit->setObjectName("atomPathEdit");
}

void ServerView::layoutComponents()
{
    QVBoxLayout* mainLayout = new QVBoxLayout();

    QHBoxLayout* headerLayout = new QHBoxLayout();

    headerLayout->addWidget(serverAliasLabel);
//    headerLayout->addStretch(1);
//    headerLayout->addWidget(deleteButton);

    mainLayout->addItem(headerLayout);

    QHBoxLayout* fieldsLayout = new QHBoxLayout();

    fieldsLayout->addSpacing(20);

    QGridLayout* fieldsGridLayout = new QGridLayout();

    fieldsGridLayout->setVerticalSpacing(10);

    fieldsGridLayout->setColumnMinimumWidth(0, 150);
    fieldsGridLayout->setColumnMinimumWidth(1, 250);
    fieldsGridLayout->setColumnStretch(0, 0);
    fieldsGridLayout->setColumnStretch(1, 1);

    fieldsGridLayout->addWidget(serverPathLabel, 0, 0, 1, 1, Qt::AlignLeft);
    fieldsGridLayout->addWidget(serverSearchPathLabel, 1, 0, 1, 1, Qt::AlignLeft);
    fieldsGridLayout->addWidget(serverAtomPathLabel, 2, 0, 1, 1, Qt::AlignLeft);

    fieldsGridLayout->addWidget(serverPathEdit, 0, 1, 1, 1, Qt::AlignLeft);
    fieldsGridLayout->addWidget(searchPathEdit, 1, 1, 1, 1, Qt::AlignLeft);
    fieldsGridLayout->addWidget(atomPathEdit, 2, 1, 1, 1, Qt::AlignLeft);

    fieldsGridLayout->addWidget(deleteButton, 1, 2, Qt::AlignLeft);


    fieldsLayout->addItem(fieldsGridLayout);

    mainLayout->addItem(fieldsLayout);
    setLayout(mainLayout);
}

void ServerView::setWindowParameters()
{
    QFile styleSheetFile(":/qss/ServerStyle");
    styleSheetFile.open(QIODevice::ReadOnly);
    this->setStyleSheet(styleSheetFile.readAll());
}

void ServerView::deleteButtonPressed()
{
    myViewModel->requestToDeleteServer();
}

void ServerView::setConnections()
{
    connect(deleteButton, SIGNAL(clicked()), this, SLOT(deleteButtonPressed()));
    connect(myViewModel, SIGNAL(serverDeleted()), this, SIGNAL(serverDeleted()));
}
