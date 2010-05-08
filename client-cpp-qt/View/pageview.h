#ifndef PAGEVIEW_H
#define PAGEVIEW_H

#include "standardview.h"
#include "multistatebutton.h"

#include <QPushButton>
#include <QLabel>

class BookResultsViewModel;

class PageView : public StandardView
{

    Q_OBJECT

public:

    PageView(QWidget* parent, BookResultsViewModel* resultsModel);

protected:

    void createComponents();
    void layoutComponents();
    void setWindowParameters();
    void setConnections();

private slots:

    void pagesCountChanged(int newPagesCount, int startingPage);
    void pagePrevAvailabilityChanged(bool availability);
    void pageNextAvailabilityChanged(bool availability);
    void pageClicked();
    void pageChanged(int page);

private:

    void hideButtons();
    void highlightSelectedPageButton();

private:

    QLabel* myPageLabel;
    QVector<MultiStateButton*> myPageButtons;
    QPushButton* myPrevButton;
    QPushButton* myNextButton;

    int myCurrentPage;

    BookResultsViewModel* myViewModel;
};


#endif // PAGEVIEW_H
