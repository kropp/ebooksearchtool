#ifndef SEARCHVIEW_H
#define SEARCHVIEW_H

class SearchViewModel;

class BookResultsRearrangeView;

class QLabel;
class QLineEdit;
class QPushButton;
class QProgressBar;

#include "standardcontentview.h"
#include "selectionfilterview.h"
#include "informationview.h"
#include "progressview.h"

class SearchView : public StandardContentView
{

Q_OBJECT

public:

    SearchView(QWidget* parent, SearchViewModel* searchViewModel);
    ~SearchView();

protected:

    void createComponents();
    void layoutComponents();
    void setWindowParameters();
    void setConnections();

    void keyPressEvent(QKeyEvent* event);

    virtual void addItemsToLeftBarPartLayout(QHBoxLayout* leftPartLayout);
    virtual void addItemsToRightBarPartLayout(QHBoxLayout* rightPartLayout);
    virtual void addItemsToLeftContentPartLayout(QHBoxLayout* leftPartLayout);
    virtual void addItemsToRightContentPartLayout(QHBoxLayout* rightPartLayout);

private slots:

    void goButtonPressed();
    void viewModelSearchResultsVisibilityChanged(bool visibility);
    void textEdited(QString newText);
    void moreAvailabilityChanged(bool);
//    void showProgress();
//    void hideProgress();

private:

    QLabel* searchLabel;
    QLineEdit* searchLine;
    QPushButton* searchButton;
    QPushButton* stopButton;
    ProgressView* myProgressBar;

    SearchViewModel* viewModel;

    BookResultsRearrangeView* myBookResults;
    SelectionFilterView* myBookFilter;
    InformationView* myInformationView;

    QString lastSearchString;

};

#endif // SEARCHVIEW_H
