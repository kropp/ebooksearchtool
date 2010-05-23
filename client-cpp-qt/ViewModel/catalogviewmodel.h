#ifndef CATALOGVIEWMODEL_H
#define CATALOGVIEWMODEL_H

#include <QObject>

class Catalog;
class CatalogResultsViewModel;
class CatalogBookResultsViewModel;
class CatalogBrowseBarViewModel;

class CatalogViewModel : public QObject
{

    Q_OBJECT

public:

    CatalogViewModel();

public:

    CatalogBookResultsViewModel* getBookResultsViewModel();
    CatalogResultsViewModel* getFolderResultsViewModel();
    CatalogBrowseBarViewModel* getBrowseBarPanelModel();

signals:

    void CatalogBookResultsChanged();
    void CatalogFolderResultsChanged();

private slots:

    void currentCatalogChanged(Catalog* catalog);
    void catalogsRecreated();

private:

    void setConnections();

private:

    CatalogBookResultsViewModel* catalogBookResults;
    CatalogResultsViewModel* catalogFolderResults;
    CatalogBrowseBarViewModel* catalogBar;

    Catalog* currentCatalog;

};

#endif // CATALOGVIEWMODEL_H
