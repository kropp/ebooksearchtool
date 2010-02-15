#include "chooseServerDialog.h"

#include <QDebug>

static const QString SERVER_FEEDBOOKS = "http://feedbooks.com";
static const QString OPENSEARCH_FEEDBOOKS = "/books/search.atom?query=";
static const QString SERVER_BOOKSERVER = "http://bookserver.archive.org";
static const QString OPENSEARCH_BOOKSERVER = "/catalog/opensearch?q=";
static const QString SERVER_SMASHWORDS = "http://smashwords.com";
static const QString OPENSEARCH_SMASHWORDS = "/atom/search/books?query=";
static const QString SERVER_MANYBOOKS = "http://manybooks.net";
static const QString OPENSEARCH_MANYBOOKS = "/stanza/search.php?q=";
static const QString SERVER_ONLY_MAWHRIN = "http://only.mawhrin.net/ebooks";
static const QString OPENSEARCH_ONLY_MAWHRIN = "/search.atom?query=";
   
typedef std::map<const QString, const QString>::const_iterator MapIt;

ChooseServerDialog::
ChooseServerDialog(QWidget* parent) : QDialog(parent) {
    qDebug() << "ChooseServerDialog:: construct";
    initialiseMap();
    createRadioButtons();
    setWindowTitle(tr("set server"));
}

void ChooseServerDialog::createRadioButtons() {
    for (MapIt it = myServers.begin(); it != myServers.end(); ++it) {
        qDebug() << it->first;
        //make radio button
    }
}

void ChooseServerDialog::initialiseMap(){
    myServers.insert(std::make_pair(SERVER_FEEDBOOKS, OPENSEARCH_FEEDBOOKS));
    
    myServers.insert(std::make_pair(SERVER_BOOKSERVER, OPENSEARCH_BOOKSERVER));

    myServers.insert(std::make_pair(SERVER_ONLY_MAWHRIN, OPENSEARCH_ONLY_MAWHRIN));
    
    myServers.insert(std::make_pair(SERVER_MANYBOOKS, OPENSEARCH_MANYBOOKS));
    
    myServers.insert(std::make_pair(SERVER_SMASHWORDS, OPENSEARCH_SMASHWORDS));
}
