#ifndef SEARCHVIEWMODEL_H
#define SEARCHVIEWMODEL_H

#include <QObject>
#include <QVector>

#include "informationviewmodel.h"

class Book;
class BookResultsViewModel;


class SearchViewModel : public QObject
{

Q_OBJECT
    
public:
    SearchViewModel();

signals:

    void resultsAvailabilityChanged(bool available);
    void infoOpenRequested(Book* book);

public:

    BookResultsViewModel* getBookResultsViewModel();
    InformationViewModel* getBookInfoViewModel();


public slots:

    void searchStartRequested(QString searchRequest);
    void stopRequested();
    void bookResultsVisibilityChanged(bool visibility);

private:

    void setSearchResultsAvailability(bool availability);

    void setConnections();

private:

    bool resultsAvailability;

    BookResultsViewModel* bookResultsVm;
    InformationViewModel* myBookInfoVm;
};

inline InformationViewModel* SearchViewModel::getBookInfoViewModel() {
    return myBookInfoVm;
}


#endif // SEARCHVIEWMODEL_H
