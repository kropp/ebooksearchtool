#ifndef PROGRAMMODESELECTORVIEW_H
#define PROGRAMMODESELECTORVIEW_H

#include "standardview.h"
#include "multistatebutton.h"
#include "../ViewModel/programmodeviewmodel.h"

#include <QLabel>

class SearchView;

class ProgramModeSelectorView : public StandardView
{

    Q_OBJECT

public:

    ProgramModeSelectorView(QWidget* parent, ProgramModeViewModel* resultsModel);

protected:

    void createComponents();
    void layoutComponents();
    void setWindowParameters();
    void setConnections();

private slots:

    void modeButtonPressed();
    void viewModeChanged(ProgramMode newMode);

private:

    void hideAllModeChildren();
    void grayAllButtons();
    void enableSearchButton();

private:

    ProgramModeViewModel* myViewModel;

    SearchView* mySearchView;
// TODO myLibraryView
    //  myCatalogView

    QFrame* myHeaderRightImage;
    QLabel* myHeaderStretchImage;
    QLabel* myHeaderLeftImage;

    QFrame* mySelectorFrame;
    MultiStateButton* mySearchModeButton;
};

#endif // PROGRAMMODESELECTORVIEW_H
