#include "librarybookresultsviewmodel.h"

#include "../Model/librarymanager.h"

LibraryBookResultsViewModel::LibraryBookResultsViewModel()
{
}

void LibraryBookResultsViewModel::initialize()
{
    setConnections();
}

void LibraryBookResultsViewModel::setConnections()
{
    connect(LibraryManager::getInstance(), SIGNAL(booksChanged(QVector<Book*>&)), this, SLOT(newBooksReceived(QVector<Book*>&)));
}
