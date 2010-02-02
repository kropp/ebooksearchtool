#include <QGridLayout>
#include <QRadioButton>
#include <QButtonGroup>
#include <QLabel>
#include <QPushButton>
#include <QLineEdit>

#include "internetConnectionDialog.h"
#include "../../network/networkmanager.h"

InternetConnectionDialog::
InternetConnectionDialog(QWidget* parent) : QDialog (parent) {
    
    // create radio buttons
    myWithoutProxyButton = new QRadioButton(tr("Without proxy"), this);
    mySetProxyButton = new QRadioButton(tr("Set proxy"), this);
    QButtonGroup* buttonGroup = new QButtonGroup(this);
    buttonGroup->addButton(myWithoutProxyButton);
    buttonGroup->addButton(mySetProxyButton);

    //set connections
    connect(myWithoutProxyButton, SIGNAL(clicked()), this, SLOT(setProxyDisabled()));
    connect(mySetProxyButton, SIGNAL(clicked()), this, SLOT(setProxyEnabled()));
    
    // create other widgets
    myProxyLabel = new QLabel(tr("Proxy: "));   
    myPortLabel = new QLabel(tr("Port: "));   

    myProxy = new QLineEdit();   
    myPort = new QLineEdit();   
   
    QPushButton* applyButton = new QPushButton(tr("Apply"));
    QPushButton* cancelButton = new QPushButton(tr("Cancel"));
    
// add widgets into layout
    QGridLayout* mainLayout = new QGridLayout();
    mainLayout->addWidget(myWithoutProxyButton, 0, 0, 1, 3);
    mainLayout->addWidget(mySetProxyButton, 1, 0, 1, 3);
    mainLayout->addWidget(myProxyLabel, 2, 1);
    mainLayout->addWidget(myPortLabel, 3, 1);
    mainLayout->addWidget(myProxy, 2, 2);
    mainLayout->addWidget(myPort, 3, 2);
    mainLayout->addWidget(applyButton, 4, 0);
    mainLayout->addWidget(cancelButton, 4, 2);

// set initial values for port and proxy
    setInitialValues();

    setLayout(mainLayout);
    setWindowTitle(tr("Internet connection settings"));
}

void InternetConnectionDialog::setInitialValues() {
    NetworkManager* manager = NetworkManager::getInstance();
    const QString& proxy = manager->getProxy();
    if (proxy.isEmpty()) {
        myWithoutProxyButton->setChecked(true);
        setProxyDisabled();
    } else {
        mySetProxyButton->setChecked(true);
        myProxy->setText(proxy);
        QString port;
        port.setNum(manager->getPort());
        myPort->setText(port);
    }
}

void InternetConnectionDialog::closeEvent(QCloseEvent*) {
    hide();
    // сверить настройки и значения полей в диалоге
}

void InternetConnectionDialog::setProxyDisabled() {
    myPort->setEnabled(false);
    myProxy->setEnabled(false);
    myProxyLabel->setEnabled(false);
    myPortLabel->setEnabled(false);
}

void InternetConnectionDialog::setProxyEnabled() {
    myPort->setEnabled(true);
    myProxy->setEnabled(true);
    myProxyLabel->setEnabled(true);
    myPortLabel->setEnabled(true);
}
