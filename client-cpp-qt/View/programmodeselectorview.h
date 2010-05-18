#ifndef PROGRAMMODESELECTORVIEW_H
#define PROGRAMMODESELECTORVIEW_H

#include "standardview.h"
#include "multistatebutton.h"
#include "../ViewModel/programmodeviewmodel.h"

#include <QLabel>

class SearchView;
class LibraryView;
class CatalogView;
class OptionsView;

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
    void enableLibraryButton();
    void enableCatalogButton();
    void enableOptionsButton();

private:

    ProgramModeViewModel* viewModel;

    SearchView* searchView;
    LibraryView* libraryView;
    CatalogView* catalogView;
    OptionsView* optionsView;

    QFrame* headerRightImage;
    QLabel* headerStretchImage;
    QLabel* headerLeftImage;

    QFrame* selectorFrame;
    MultiStateButton* searchModeButton;
    MultiStateButton* libraryModeButton;
    MultiStateButton* catalogModeButton;
    MultiStateButton* optionsModeButton;
};

#endif // PROGRAMMODESELECTORVIEW_H
