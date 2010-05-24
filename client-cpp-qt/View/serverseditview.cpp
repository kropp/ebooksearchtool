#include "serverseditview.h"
#include "../ViewModel/serverseditviewmodel.h"
#include "../ViewModel/servervm.h"

#include <QFile>
#include <QVBoxLayout>

const int SERVER_LINE_HEIGHT = 150;

const int ADDITIONAL_HEIGHT = 50;

static const QString NEW_SERVER_LABEL = "Add new server";
static const QString ALIAS_LABEL = "Set alias:";
static const QString SERVER_PATH_LABEL = "Set path:";
static const QString SERVER_SEARCH_PATH_LABEL = "Set book search path:";
static const QString SERVER_ATOM_PATH_LABEL = "Set Atom path:";
static const QString DROP_SERVERS_LABEL = "Set default servers";

ServersEditView::ServersEditView(QWidget* parent, ServersEditViewModel* viewModel)
        :StandardView(parent)
{
    myViewModel = viewModel;
    initialize();
}

ServersEditView::~ServersEditView()
{

}

void ServersEditView::initialize()
{
    createComponents();
    layoutComponents();
    setWindowParameters();
    setConnections();
}

void ServersEditView::recreateServerViews()
{
    serverViews.clear();

    foreach (ServerViewModel* serverViewModel, myViewModel->getServerViewModels())
    {
        ServerView* newServerView = new ServerView(this, serverViewModel);
        serverViews.append(newServerView);
    }
}

void ServersEditView::serversChanged()
{
    foreach (ServerView* serverView, serverViews)
    {
        //layout()->removeWidget(serverView);
        delete serverView;
    }

    recreateServerViews();

    delete layout();

    layoutComponents();
}

void ServersEditView::createComponents()
{
    serversLabel = new QLabel(this);
    serversLabel->setObjectName("serversLabel");
    serversLabel->setText("Servers list:");

    recreateServerViews();

    newServerLabel = new QLabel(this);
    aliasLabel = new QLabel(this);
    serverPathLabel = new QLabel(this);
    serverSearchPathLabel = new QLabel(this);
    serverAtomPathLabel = new QLabel(this);

    newServerLabel->setText(NEW_SERVER_LABEL);
    aliasLabel->setText(ALIAS_LABEL);
    serverPathLabel->setText(SERVER_PATH_LABEL);
    serverSearchPathLabel->setText(SERVER_SEARCH_PATH_LABEL);
    serverAtomPathLabel->setText(SERVER_ATOM_PATH_LABEL);

    newServerLabel->setObjectName("serverAliasLabel");
    serverPathLabel->setObjectName("serverPathLabel");
    serverSearchPathLabel->setObjectName("serverSearchPathLabel");
    serverAtomPathLabel->setObjectName("serverAtomPathLabel");

    addServerButton = new QPushButton(this);
    addServerButton->setObjectName("addServer");

    aliasEdit = new QLineEdit(this);
    serverPathEdit = new QLineEdit(this);
    searchPathEdit = new QLineEdit(this);
    atomPathEdit = new QLineEdit(this);

    serverPathEdit->setText("");
    searchPathEdit->setText("");
    atomPathEdit->setText("");
    aliasEdit->setText("");

    serverPathEdit->setObjectName("serverPathEdit");
    searchPathEdit->setObjectName("searchPathEdit");
    atomPathEdit->setObjectName("atomPathEdit");
    aliasEdit->setObjectName("aliasEdit");

    dropServersButton = new QPushButton(this);
    dropServersButton->setText(DROP_SERVERS_LABEL);
}

void ServersEditView::layoutComponents()
{
    QVBoxLayout* mainLayout = new QVBoxLayout();
    mainLayout->setMargin(0);
    mainLayout->setSpacing(0);

    mainLayout->addWidget(serversLabel, Qt::AlignLeft);
    mainLayout->addSpacing(20);

    foreach (ServerView* serverView, serverViews)
    {
        mainLayout->addWidget(serverView);
        mainLayout->addSpacing(20);
    }

    QVBoxLayout* newServerLayout = new QVBoxLayout();

    newServerLayout->setMargin(10);

    QHBoxLayout* headerLayout = new QHBoxLayout();

    headerLayout->addWidget(newServerLabel);
//    headerLayout->addStretch(1);
//    headerLayout->addWidget(addServerButton);

    newServerLayout->addItem(headerLayout);

    QHBoxLayout* fieldsLayout = new QHBoxLayout();

    fieldsLayout->addSpacing(20);

    QGridLayout* fieldsGridLayout = new QGridLayout();

    fieldsGridLayout->setVerticalSpacing(10);

    fieldsGridLayout->setColumnMinimumWidth(0, 150);
    fieldsGridLayout->setColumnMinimumWidth(1, 250);
    fieldsGridLayout->setColumnStretch(0, 0);
    fieldsGridLayout->setColumnStretch(1, 1);

    fieldsGridLayout->addWidget(aliasLabel, 0, 0, 1, 1, Qt::AlignLeft);
    fieldsGridLayout->addWidget(serverPathLabel, 1, 0, 1, 1, Qt::AlignLeft);
    fieldsGridLayout->addWidget(serverSearchPathLabel, 2, 0, 1, 1, Qt::AlignLeft);
    fieldsGridLayout->addWidget(serverAtomPathLabel, 3, 0, 1, 1, Qt::AlignLeft);

    fieldsGridLayout->addWidget(aliasEdit, 0, 1, 1, 1, Qt::AlignLeft);
    fieldsGridLayout->addWidget(serverPathEdit, 1, 1, 1, 1, Qt::AlignLeft);
    fieldsGridLayout->addWidget(searchPathEdit, 2, 1, 1, 1, Qt::AlignLeft);
    fieldsGridLayout->addWidget(atomPathEdit, 3, 1, 1, 1, Qt::AlignLeft);


    fieldsGridLayout->addWidget(addServerButton, 1, 2, Qt::AlignCenter);


    fieldsLayout->addItem(fieldsGridLayout);

    newServerLayout->addItem(fieldsLayout);

    mainLayout->addItem(newServerLayout);
    mainLayout->addSpacing(20);
    mainLayout->addWidget(dropServersButton, Qt::AlignLeft);

    setLayout(mainLayout);

    this->setFixedHeight(ADDITIONAL_HEIGHT + (serverViews.count() + 1) * SERVER_LINE_HEIGHT);
}

void ServersEditView::setWindowParameters()
{
    QFile styleSheetFile(":/qss/ServersEditStyle");
    styleSheetFile.open(QIODevice::ReadOnly);
    this->setStyleSheet(styleSheetFile.readAll());
}

void ServersEditView::setConnections()
{
    connect(myViewModel, SIGNAL(serverVmsChanged()), this, SLOT(serversChanged()));
    connect(addServerButton, SIGNAL(clicked()), this, SLOT(addServerPressed()));
    connect(dropServersButton, SIGNAL(clicked()), this, SLOT(defaultServersPressed()));
}

void ServersEditView::addServerPressed()
{
    myViewModel->requestToAddServer(aliasEdit->text(), serverPathEdit->text(), searchPathEdit->text(), atomPathEdit->text(), true ,true);
}

void ServersEditView::defaultServersPressed()
{
    myViewModel->dropServersToDefault();
}
