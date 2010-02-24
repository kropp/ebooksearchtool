#include "chooseServerDialog.h"
#include "../searchwidget.h"
#include "../../network/networkmanager.h"

#include <QButtonGroup>
#include <QRadioButton>
#include <QVBoxLayout>

#include <QDebug>

ChooseServerDialog::
ChooseServerDialog(QWidget* parent) : QDialog(parent) {
    myMainLayout = new QVBoxLayout();
    createRadioButtons();
    setLayout(myMainLayout);
    setWindowTitle(tr("set server"));
}

void ChooseServerDialog::createRadioButtons() {
    myButtonGroup = new QButtonGroup(this);
    QList<QString> servers;
    NetworkManager::getInstance()->getServers(servers);
    qDebug() << "servers from manager: " << servers;
    foreach (QString server, servers) {
        //qDebug() << it->first;
        QRadioButton* button = new QRadioButton(server, this);
        if (server == NetworkManager::getCurrentServer()) {
            button->setChecked(true);
        }
        myButtonGroup->addButton(button);
        myMainLayout->addWidget(button);  
        connect(button, SIGNAL(clicked()), this, SLOT(chooseServer()));
    }
}

void ChooseServerDialog::chooseServer() {
     QString server = myButtonGroup->checkedButton()->text();
     qDebug() << "ChooseServerDialog::chooseServer " << server;
     NetworkManager::setServer(server); 
}
