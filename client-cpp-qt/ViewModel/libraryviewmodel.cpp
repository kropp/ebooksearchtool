#include "libraryviewmodel.h"
#include "librarybookresultsviewmodel.h"
#include "../Model/librarymanager.h"

LibraryViewModel::LibraryViewModel()
{
    viewModel = new LibraryBookResultsViewModel();
    viewModel->initialize();

    setConnections();
}

LibraryBookResultsViewModel* LibraryViewModel::getBookResultsViewModel()
{
    return viewModel;
}

void LibraryViewModel::setConnections()
{
    connect(LibraryManager::getInstance(), SIGNAL(booksAvailabilityChanged(bool)), this, SIGNAL(libraryResultsAvailabilityChanged(bool)));
}
