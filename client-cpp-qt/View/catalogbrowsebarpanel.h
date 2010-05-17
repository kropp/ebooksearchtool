#ifndef CATALOGBROWSEBARPANEL_H
#define CATALOGBROWSEBARPANEL_H

#include "standardview.h"

#include <QPushButton>
#include <QFrame>
#include <QProgressBar>

class CatalogBrowseBarViewModel;

class CatalogBrowseBarPanel : public StandardView
{

    Q_OBJECT

public:

    CatalogBrowseBarPanel(QWidget* parent, CatalogBrowseBarViewModel* resultsModel);

protected:

    void createComponents();
    void layoutComponents();
    void setWindowParameters();
    void setConnections();

private slots:

    void setUpAvailability(bool newValue);
    void setBackAvailability(bool newValue);
    void setForwardAvailability(bool newValue);
    void showProgress();
    void hideProgress();

private:

    QPushButton* myHomeButton;
    QPushButton* myGoUpButton;
    QPushButton* myGoBackButton;
    QPushButton* myGoForwardButton;
    QFrame* myBarFrame;
    QProgressBar* myProgressBar;

    CatalogBrowseBarViewModel* myViewModel;
};

#endif // CATALOGBROWSEBARPANEL_H
