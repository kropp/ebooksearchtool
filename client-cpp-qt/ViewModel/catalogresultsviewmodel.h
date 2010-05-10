#ifndef CATALOGRESULTSVIEWMODEL_H
#define CATALOGRESULTSVIEWMODEL_H

#include <QObject>

class Catalog;
class CatalogResultViewModel;

class CatalogResultsViewModel : public QObject
{

    Q_OBJECT

public:

    CatalogResultsViewModel();

public:

    void changeCatalog(Catalog* newCatalog);
    QVector<CatalogResultViewModel*>* getShownCatalogs();

signals:

    void shownCatalogsChanged(QVector<CatalogResultViewModel*>* newCatalogs);

private:

    void createChildViewModels();

private:

    QVector<CatalogResultViewModel*>* shownCatalogViewModels;
    Catalog* shownCatalog;
    bool isTopLevel;

};

#endif // CATALOGRESULTSVIEWMODEL_H
