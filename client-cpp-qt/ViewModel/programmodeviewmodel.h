#ifndef PROGRAMMODEVIEWMODEL_H
#define PROGRAMMODEVIEWMODEL_H

#include <QObject>

enum ProgramMode {SEARCH}; //, LIBRARY, CATALOG};

class Book;
class SearchViewModel;
class LibraryViewModel;

class ProgramModeViewModel  : public QObject
{

Q_OBJECT

public:

    ProgramModeViewModel();

public slots:

    void requestToChangeProgramMode(ProgramMode newMode);
   // void infoOpenRequested(Book* book);

signals:

    void viewModeChanged(ProgramMode newMode);

public:

    ProgramMode getCurrentMode();
    SearchViewModel* getSearchViewModel();
    LibraryViewModel* getLibraryViewModel();

private:

    void changeViewMode(ProgramMode newMode);
    void setConnections();

private:

    ProgramMode myCurrentMode;

    SearchViewModel* mySearchViewModel;
    //LibraryViewModel* myLibraryViewModel;
};

#endif // PROGRAMMODEVIEWMODEL_H
