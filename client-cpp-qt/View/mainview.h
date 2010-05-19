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

//
//private:
//    void closeEvent (QCloseEvent * e );

private:


    ProgramModeSelectorView* programSelectorView;

};

#endif // MAINVIEW_H
