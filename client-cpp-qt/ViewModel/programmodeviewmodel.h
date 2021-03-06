#ifndef PROGRAMMODEVIEWMODEL_H
#define PROGRAMMODEVIEWMODEL_H

#include <QObject>

enum ProgramMode {SEARCH, LIBRARY, CATALOG, OPTIONS};

class Book;
class SearchViewModel;
class LibraryViewModel;
class CatalogViewModel;
class OptionsViewModel;

class ProgramModeViewModel  : public QObject
{

Q_OBJECT

public:

    ProgramModeViewModel();

public slots:

    void requestToChangeProgramMode(ProgramMode newMode);
    void infoOpenRequested(Book* book);

signals:

    void viewModeChanged(ProgramMode newMode);

public:

    ProgramMode getCurrentMode();
    SearchViewModel* getSearchViewModel();
    LibraryViewModel* getLibraryViewModel();
    CatalogViewModel* getCatalogViewModel();
    OptionsViewModel* getOptionsViewModel();

private:

    void changeViewMode(ProgramMode newMode);
    void setConnections();

private:

    ProgramMode currentMode;

    SearchViewModel* searchViewModel;
    LibraryViewModel* libraryViewModel;
    CatalogViewModel* catalogViewModel;
    OptionsViewModel* optionsViewModel;

    bool myLibraryWasOpened;
};

#endif // PROGRAMMODEVIEWMODEL_H
