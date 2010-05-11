#ifndef CATALOGRESULTVIEWMODEL_H
#define CATALOGRESULTVIEWMODEL_H

#include <QObject>

class Catalog;

class CatalogResultViewModel : public QObject
{

Q_OBJECT

public:

    CatalogResultViewModel(Catalog* catalog);


public:

    QString getCatalogName();
    QString getCatalogSummary();

public slots:

    void openCatalogRequested();

signals:


private slots:


private:

    void setConnections();

private:

    Catalog* shownCatalog;

};
#endif // CATALOGRESULTVIEWMODEL_H
