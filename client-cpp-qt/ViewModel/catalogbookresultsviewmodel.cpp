#include "catalogbookresultsviewmodel.h"

#include "../Model/catalog.h"
#include "../Model/catalogmanager.h"

CatalogBookResultsViewModel::CatalogBookResultsViewModel()
{
}

void CatalogBookResultsViewModel::initialize()
{
    setConnections();
}

void CatalogBookResultsViewModel::changeCatalog(Catalog* newCatalog)
{
    shownCatalog = newCatalog;
    newBooksReceived(*newCatalog->getBooks());
}

void CatalogBookResultsViewModel::setConnections()
{

}
