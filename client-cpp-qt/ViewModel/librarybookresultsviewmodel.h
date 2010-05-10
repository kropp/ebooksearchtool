#ifndef LIBRARYBOOKRESULTSVIEWMODEL_H
#define LIBRARYBOOKRESULTSVIEWMODEL_H

#include "bookresultsviewmodel.h"

class LibraryBookResultsViewModel : public BookResultsViewModel
{

public:

    LibraryBookResultsViewModel();

public:

    void initialize();

protected:

    void setConnections();

};

#endif // LIBRARYBOOKRESULTSVIEWMODEL_H
