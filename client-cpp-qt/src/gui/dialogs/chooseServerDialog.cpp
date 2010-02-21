#include "chooseServerDialog.h"
#include "../searchwidget.h"
#include "../../network/networkmanager.h"

#include <QButtonGroup>
#include <QRadioButton>
#include <QVBoxLayout>

#include <QDebug>

static const QString SERVER_FEEDBOOKS = "feedbooks.com";
static const QString OPENSEARCH_FEEDBOOKS = "/books/search.atom?query=";
static const QString SERVER_BOOKSERVER = "bookserver.archive.org";
static const QString OPENSEARCH_BOOKSERVER = "/catalog/opensearch?q=";
static const QString SERVER_SMASHWORDS = "smashwords.com";
static const QString OPENSEARCH_SMASHWORDS = "/atom/search/books?query=";
static const QString SERVER_MANYBOOKS = "manybooks.net";
static const QString OPENSEARCH_MANYBOOKS = "/stanza/search.php?q=";
static const QString SERVER_ONLY_MAWHRIN = "only.mawhrin.net/ebooks";
static const QString OPENSEARCH_ONLY_MAWHRIN = "/search.atom?query=";
   
typedef std::map<const QString, const QString>::const_iterator MapIt;

ChooseServerDialog::
ChooseServerDialog(QWidget* parent) : QDialog(parent) {
    initialiseMap();
    myMainLayout = new QVBoxLayout();
    createRadioButtons();
    setLayout(myMainLayout);
    setWindowTitle(tr("set server"));
}

void ChooseServerDialog::createRadioButtons() {
    myButtonGroup = new QButtonGroup(this);
    for (MapIt it = myServers.begin(); it != myServers.end(); ++it) {
        //qDebug() << it->first;
        QRadioButton* button = new QRadioButton(it->first, this);
        if (it->first == NetworkManager::getServer()) {
            button->setChecked(true);
        }
        myButtonGroup->addButton(button);
        myMainLayout->addWidget(button);  
        connect(button, SIGNAL(clicked()), this, SLOT(chooseServer()));
    }
}

void ChooseServerDialog::initialiseMap(){
    myServers.insert(std::make_pair(SERVER_FEEDBOOKS, OPENSEARCH_FEEDBOOKS));
    
    myServers.insert(std::make_pair(SERVER_BOOKSERVER, OPENSEARCH_BOOKSERVER));

    myServers.insert(std::make_pair(SERVER_ONLY_MAWHRIN, OPENSEARCH_ONLY_MAWHRIN));
    
    myServers.insert(std::make_pair(SERVER_MANYBOOKS, OPENSEARCH_MANYBOOKS));
    
    myServers.insert(std::make_pair(SERVER_SMASHWORDS, OPENSEARCH_SMASHWORDS));
}

void ChooseServerDialog::chooseServer() {
     qDebug() << "ChooseServerDialog::chooseServer";
     QString server = myButtonGroup->checkedButton()->text();
     QString opensearch = myServers.find(server)->second;
     qDebug() << "server + search" << server + opensearch;
     SearchWidget::setOpensearchSchema(opensearch);
     NetworkManager::setServer(server); 
}
