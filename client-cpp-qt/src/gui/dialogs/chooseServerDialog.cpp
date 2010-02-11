#include "chooseServerDialog.h"

#include <QDebug>

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
    myServers.insert(std::make_pair("feedbooks.com",
                     "http://feedbooks.com/books/search.atom?query="));
    
    myServers.insert(std::make_pair("bookserver.archive.org",
                     "http://bookserver.archive.org/catalog/opensearch?q="));

    myServers.insert(std::make_pair("smashwords.com",
                     "http://www.smashwords.com/atom/search/books?query="));
    
    myServers.insert(std::make_pair("manybooks.net",
                    "http://manybooks.net/stanza/search.php?q="));
}
