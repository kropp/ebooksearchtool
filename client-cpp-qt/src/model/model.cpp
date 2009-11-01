#include <QModelIndex>
#include <iostream>

#include "model.h"

Model::Model (Data* data) : myData (data) {
    std::cout << "model initialized\n";
}


Model::~Model() {}

QModelIndex Model::index (int , int, const QModelIndex& ) const { // return valid index
    return QModelIndex();
}
 
QMap<int, QVariant> Model::itemData (const QModelIndex& ) const {
    return QMap<int, QVariant> ();
}
  
int Model::rowCount (const QModelIndex& ) const {
    return myData->getSize();
}

QModelIndex Model::parent(const QModelIndex& ) const {
    return QModelIndex();
}

int Model::columnCount(const QModelIndex& ) const {
    return 0;
}
    
QVariant Model::data (const QModelIndex&, int ) const {
    return QVariant();
}

