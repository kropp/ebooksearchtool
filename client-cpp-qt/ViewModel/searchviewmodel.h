#ifndef SEARCHVIEWMODEL_H
#define SEARCHVIEWMODEL_H

#include <QObject>
#include <QVector>

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

public slots:

    void searchStartRequested(QString searchRequest);
    void moreBooksRequested();
    void bookResultsVisibilityChanged(bool visibility);

private:

    void setSearchResultsAvailability(bool availability);

    void setConnections();

private:

    bool myResultsAvailability;

    BookResultsViewModel* myBookResultsVm;
};
#endif // SEARCHVIEWMODEL_H
