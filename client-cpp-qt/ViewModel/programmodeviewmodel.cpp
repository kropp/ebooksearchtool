#include "programmodeviewmodel.h"
#include "searchviewmodel.h"
#include "libraryviewmodel.h"
#include "catalogviewmodel.h"
#include "../Model/book.h"
#include "../Model/librarymanager.h"

ProgramModeViewModel::ProgramModeViewModel()
{
    currentMode = SEARCH;
    myLibraryWasOpened = false;

    searchViewModel = new SearchViewModel();
    libraryViewModel = new LibraryViewModel();
    catalogViewModel = new CatalogViewModel();

    setConnections();
}

void ProgramModeViewModel::requestToChangeProgramMode(ProgramMode newMode)
{
    changeViewMode(newMode);
}

void ProgramModeViewModel::infoOpenRequested(Book* /*book*/)
{

}

void ProgramModeViewModel::setConnections()
{
    connect(searchViewModel, SIGNAL(infoOpenRequested(Book*)), this, SLOT(infoOpenRequested(Book*)));
}

void ProgramModeViewModel::changeViewMode(ProgramMode newMode)
{
    currentMode = newMode;
    if ((newMode == LIBRARY) && (!myLibraryWasOpened)) {
        LibraryManager::getInstance()->openLibrary();
        myLibraryWasOpened = true;
    }
    emit viewModeChanged(currentMode);
}

ProgramMode ProgramModeViewModel::getCurrentMode()
{
    return currentMode;
}

CatalogViewModel* ProgramModeViewModel::getCatalogViewModel()
{
    return catalogViewModel;
}

SearchViewModel* ProgramModeViewModel::getSearchViewModel()
{
    return searchViewModel;
}

LibraryViewModel* ProgramModeViewModel::getLibraryViewModel()
{
    return libraryViewModel;
}
