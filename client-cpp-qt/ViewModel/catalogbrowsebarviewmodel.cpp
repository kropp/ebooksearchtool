#include "catalogbrowsebarviewmodel.h"

#include "../Model/catalogmanager.h"
#include "../Model/catalog.h"

CatalogBrowseBarViewModel::CatalogBrowseBarViewModel()
{
    setConnections();
}

void CatalogBrowseBarViewModel::goBack()
{
    CatalogManager::getInstance()->goBack();
}

void CatalogBrowseBarViewModel::goForward()
{
    CatalogManager::getInstance()->goForward();
}

void CatalogBrowseBarViewModel::goHome()
{
    CatalogManager::getInstance()->openRoot();
}

void CatalogBrowseBarViewModel::goUp()
{
    CatalogManager::getInstance()->goUpLevel();
}

bool CatalogBrowseBarViewModel::getGoBackAvailability()
{
    return CatalogManager::getInstance()->goBackAvailable();
}

bool CatalogBrowseBarViewModel::getGoForwardAvailability()
{
    return CatalogManager::getInstance()->goForwardAvailable();
}

bool CatalogBrowseBarViewModel::getGoUpAvailability()
{
    return CatalogManager::getInstance()->goUpAvailable();
}

void CatalogBrowseBarViewModel::setConnections()
{
    connect(CatalogManager::getInstance(), SIGNAL(goUpAvailabilityChanged(bool)), this, SIGNAL(upAvailabilityChanged(bool)));
    connect(CatalogManager::getInstance(), SIGNAL(goBackAvailabilityChanged(bool)), this, SIGNAL(backAvailabilityChanged(bool)));
    connect(CatalogManager::getInstance(), SIGNAL(goForwardAvailabilityChanged(bool)), this, SIGNAL(forwardAvailabilityChanged(bool)));
}
