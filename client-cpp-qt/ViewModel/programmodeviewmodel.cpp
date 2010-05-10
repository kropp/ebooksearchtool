#include "programmodeviewmodel.h"
#include "searchviewmodel.h"
#include "libraryviewmodel.h"
#include "catalogviewmodel.h"
#include "../Model/book.h"

ProgramModeViewModel::ProgramModeViewModel()
{
    currentMode = SEARCH;

    searchViewModel = new SearchViewModel();
    libraryViewModel = new LibraryViewModel();
    catalogViewModel = new CatalogViewModel();

    setConnections();
}

void ProgramModeViewModel::requestToChangeProgramMode(ProgramMode newMode)
{
    changeViewMode(newMode);
}

void ProgramModeViewModel::infoOpenRequested(Book* book)
{
    int i = 0;
}

void ProgramModeViewModel::setConnections()
{
    connect(searchViewModel, SIGNAL(infoOpenRequested(Book*)), this, SLOT(infoOpenRequested(Book*)));
}

void ProgramModeViewModel::changeViewMode(ProgramMode newMode)
{
    currentMode = newMode;
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
