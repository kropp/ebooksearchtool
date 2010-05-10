#include "bookresultviewmodel.h"
#include "bookresultsviewmodel.h"

#include "../Model/book.h"
#include "../Model/librarymanager.h"

BookResultViewModel::BookResultViewModel(Book* book, BookResultsViewModel* parent)
{
    shownBook = book;
    parentModel = parent;
}

QString BookResultViewModel::getBookName()
{
    return shownBook->getTitle().trimmed();
}

QString BookResultViewModel::getServerName()
{
    return shownBook->getServerName();
}

QString BookResultViewModel::getAuthorName()
{
    if (shownBook->getAuthors().size() > 0)
    {
        return shownBook->getAuthors().at(0)->getName().trimmed();
    }
    else
    {
        if (shownBook->getAuthors().size() == 0)
        {
            return "Unknown author";
        }
        else
        {
            return "Various authors";
        }
    }
}

QString BookResultViewModel::getLanguage()
{
    return shownBook->getLanguage();
}

void BookResultViewModel::addBookToLibraryRequested()
{
    LibraryManager::getInstance()->addBookToLibrary(shownBook);
}

void BookResultViewModel::removeBookFromLibraryRequested()
{
    LibraryManager::getInstance()->removeBookFromLibrary(shownBook);
}

void BookResultViewModel::bookInfoRequested()
{
    parentModel->bookInfoRequested(shownBook);
}
