#include "bookresultviewmodel.h"
#include "bookresultsviewmodel.h"

#include "../Model/book.h"


BookResultViewModel::BookResultViewModel(Book* book, BookResultsViewModel* parent)
{
    myShownBook = book;
    myParentModel = parent;
}

QString BookResultViewModel::getBookName()
{
    return myShownBook->getTitle().trimmed();
}

QString BookResultViewModel::getServerName()
{
    return myShownBook->getServerName();
}

QString BookResultViewModel::getAuthorName()
{
    if (myShownBook->getAuthors().size() > 0)
    {
        return myShownBook->getAuthors().at(0)->getName().trimmed();
    }
    else
    {
        if (myShownBook->getAuthors().size() == 0)
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
    return myShownBook->getLanguage();
}

void BookResultViewModel::addBookToLibraryRequested()
{

}

void BookResultViewModel::removeBookFromLibraryRequested()
{

}

void BookResultViewModel::bookInfoRequested()
{
    myParentModel->bookInfoRequested(myShownBook);
}
