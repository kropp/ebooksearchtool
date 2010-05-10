#ifndef CATALOGBOOKRESULTSVIEWMODEL_H
#define CATALOGBOOKRESULTSVIEWMODEL_H

#include "bookresultsviewmodel.h"

class Catalog;

class CatalogBookResultsViewModel : public BookResultsViewModel
{

public:

    CatalogBookResultsViewModel();

public:

    void initialize();
    void changeCatalog(Catalog* newCatalog);

protected:

    void setConnections();

private:

    Catalog* shownCatalog;

};

#endif // CATALOGBOOKRESULTSVIEWMODEL_H
