#ifndef LIBRARYVIEWMODEL_H
#define LIBRARYVIEWMODEL_H

#include <QObject>

class LibraryBookResultsViewModel;

class LibraryViewModel : public QObject
{

    Q_OBJECT

public:

    LibraryViewModel();

public:

    LibraryBookResultsViewModel* getBookResultsViewModel();

signals:

    void libraryResultsAvailabilityChanged(bool availability);

protected:

    void setConnections();

private:

    LibraryBookResultsViewModel* viewModel;

};

#endif // LIBRARYVIEWMODEL_H
