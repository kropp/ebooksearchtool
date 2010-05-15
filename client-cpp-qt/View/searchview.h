#ifndef SEARCHVIEW_H
#define SEARCHVIEW_H

class SearchViewModel;

class BookResultsRearrangeView;

class QLabel;
class QLineEdit;
class QPushButton;

#include "standardcontentview.h"
#include "selectionfilterview.h"

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

    virtual void addItemsToLeftBarPartLayout(QHBoxLayout* leftPartLayout);
    virtual void addItemsToRightBarPartLayout(QHBoxLayout* rightPartLayout);
    virtual void addItemsToLeftContentPartLayout(QHBoxLayout* leftPartLayout);
    virtual void addItemsToRightContentPartLayout(QHBoxLayout* rightPartLayout);

private slots:

    void goButtonPressed();
    void viewModelSearchResultsVisibilityChanged(bool visibility);
    void textEdited(QString newText);
    void moreAvailabilityChanged(bool);

private:

    QLabel* searchLabel;
    QLineEdit* searchLine;
    QPushButton* searchButton;
    QPushButton* moreButton;

    SearchViewModel* viewModel;

    BookResultsRearrangeView* bookResults;
    SelectionFilterView* bookFilter;

    QString lastSearchString;

};

#endif // SEARCHVIEW_H
