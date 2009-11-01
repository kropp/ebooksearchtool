#ifndef _MODEL_H_
#define _MODEL_H_

#include <QAbstractItemModel>
#include <../data/data.h>

class Model : public QAbstractItemModel {
public:
    Model (Data* data);
    virtual ~Model ();

public:
    QModelIndex index (int row, int column, const QModelIndex & parent = QModelIndex()) const; // return valid index
    QMap<int, QVariant> itemData (const QModelIndex & index) const;
    int rowCount (const QModelIndex & parent = QModelIndex()) const;
    Data* getData() const;
    QModelIndex parent(const QModelIndex& index) const;
    int columnCount(const QModelIndex& index) const;
    QVariant data (const QModelIndex & index, int role = Qt::DisplayRole) const;

private:
//    QModelIndex createIndex (int row, int column, void * ptr = 0) const;

private:
    Data* myData;
    //QVariant headerData (int section, Qt::Orientation orientation, int role = Qt::DisplayRole) const;
    //bool setData (const QModelIndex & index, const QVariant & value, int role = Qt::EditRole);
    //bool setItemData (const QModelIndex & index, const QMap<int, QVariant> & roles);

//bool setHeaderData ( int section, Qt::Orientation orientation, const QVariant & value, int role = Qt::EditRole )
//insert rows, columns
};

inline Data* Model::getData() const {
    return myData;
}

#endif //_MODEL_H_
