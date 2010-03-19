#include "loader.h"
#include <QDebug>

Loader::Loader() {
    connect (NetworkManager::getInstance(), SIGNAL(requestFinished(int, bool)), 
             this, SLOT(requestFinished(int, bool)));
}
    
Loader:: ~Loader() {}

void Loader::load(QString request, QIODevice* file) {
   myRequestId = NetworkManager::getInstance()->downloadByUrl(request, file);
}

bool Loader::requestFinished(int id, bool error) { 
    if (id != myRequestId) {
        return true;
    }   
    //qDebug() << "Loader::request Finished ";
    QString result = (error) ? "ERROR!!!" : "SUCCEED"; 
    qDebug() << result;
   // exit an application
    exit(0);
    return error;
}

