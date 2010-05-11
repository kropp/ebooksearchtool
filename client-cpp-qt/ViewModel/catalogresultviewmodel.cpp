#include "catalogresultviewmodel.h"
#include "../Model/catalog.h"
#include "../Model/catalogmanager.h"

CatalogResultViewModel::CatalogResultViewModel(Catalog* catalog)
{
    shownCatalog = catalog;
}

void CatalogResultViewModel::openCatalogRequested()
{
    CatalogManager::getInstance()->openCatalog(shownCatalog);
}

QString CatalogResultViewModel::getCatalogName()
{
    return shownCatalog->getCatalogName();
}

QString CatalogResultViewModel::getCatalogSummary()
{
    return shownCatalog->getSummary();
}

