#include "programmodeviewmodel.h"
#include "searchviewmodel.h"
#include "../Model/book.h"

ProgramModeViewModel::ProgramModeViewModel()
{
    myCurrentMode = SEARCH;

    mySearchViewModel = new SearchViewModel();

    setConnections();
}

void ProgramModeViewModel::requestToChangeProgramMode(ProgramMode newMode)
{
    changeViewMode(newMode);
}

//void ProgramModeViewModel::infoOpenRequested(Book* book)
//{
//   
//}

void ProgramModeViewModel::setConnections()
{
    connect(mySearchViewModel, SIGNAL(infoOpenRequested(Book*)), this, SLOT(infoOpenRequested(Book*)));
}

void ProgramModeViewModel::changeViewMode(ProgramMode newMode)
{
    myCurrentMode = newMode;
    emit viewModeChanged(myCurrentMode);
}

ProgramMode ProgramModeViewModel::getCurrentMode()
{
    return myCurrentMode;
}

SearchViewModel* ProgramModeViewModel::getSearchViewModel()
{
    return mySearchViewModel;
}

LibraryViewModel* ProgramModeViewModel::getLibraryViewModel()
{
    return myLibraryViewModel;
}
