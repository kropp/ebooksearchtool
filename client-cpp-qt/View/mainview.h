#ifndef MAINVIEW_H
#define MAINVIEW_H

class ProgramModeSelectorView;

#include "standardview.h"

class MainView : public StandardView
{

public:

    MainView();

protected:

    void createComponents();
    void layoutComponents();
    void setWindowParameters();

private:


    ProgramModeSelectorView* myProgramSelectorView;

};

#endif // MAINVIEW_H