#include "catalogviewmodel.h"
#include "catalogresultsviewmodel.h"
#include "catalogbookresultsviewmodel.h"
#include "catalogbrowsebarviewmodel.h"

#include "../Model/catalogmanager.h"
#include "../Model/catalog.h"

CatalogViewModel::CatalogViewModel()
{
    catalogBookResults = new CatalogBookResultsViewModel();
    catalogFolderResults = new CatalogResultsViewModel();
    catalogBar = new CatalogBrowseBarViewModel();
    setConnections();

    catalogFolderResults->changeCatalog(CatalogManager::getInstance()->getCatalogRoot());
    emit CatalogFolderResultsChanged();
}

CatalogBrowseBarViewModel* CatalogViewModel::getBrowseBarPanelModel()
{
    return catalogBar;
}

CatalogBookResultsViewModel* CatalogViewModel::getBookResultsViewModel()
{
    return catalogBookResults;
}

CatalogResultsViewModel* CatalogViewModel::getFolderResultsViewModel()
{
    return catalogFolderResults;
}

void CatalogViewModel::setConnections()
{
    connect(CatalogManager::getInstance(), SIGNAL(currentCatalogChanged(Catalog*)), this, SLOT(currentCatalogChanged(Catalog*)));
}

void CatalogViewModel::currentCatalogChanged(Catalog* catalog)
{
    currentCatalog = catalog;

    if (catalog->containsBooks())
    {
        catalogBookResults->changeCatalog(catalog);
        emit CatalogBookResultsChanged();
    }
    else
    {
        catalogFolderResults->changeCatalog(catalog);
        emit CatalogFolderResultsChanged();
    }
}
