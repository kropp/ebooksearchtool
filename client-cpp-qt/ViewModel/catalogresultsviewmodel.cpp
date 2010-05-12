#include "catalogresultsviewmodel.h"
#include "catalogresultviewmodel.h"
#include "../Model/catalogmanager.h"
#include "../Model/catalog.h"

CatalogResultsViewModel::CatalogResultsViewModel()
{
    shownCatalogViewModels = new QVector<CatalogResultViewModel*>();
}

void CatalogResultsViewModel::createChildViewModels()
{
    shownCatalogViewModels->clear();

    for (int i = 0; i < shownCatalog->getCatalogs().size(); i++)
    {
        Catalog* nextChild = shownCatalog->getCatalogs().at(i);
        shownCatalogViewModels->append(new CatalogResultViewModel(nextChild));
    }
}

QVector<CatalogResultViewModel*>* CatalogResultsViewModel::getShownCatalogs()
{
    return shownCatalogViewModels;
}

void CatalogResultsViewModel::changeCatalog(Catalog* newCatalog)
{
    shownCatalog = newCatalog;

    isTopLevel = (newCatalog == CatalogManager::getInstance()->getCatalogRoot());

    createChildViewModels();

    emit shownCatalogsChanged(shownCatalogViewModels);
}
