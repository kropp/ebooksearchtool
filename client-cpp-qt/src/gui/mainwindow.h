#ifndef _MAIN_WINDOW_H
#define _MAIN_WINDOW_H

#include <QMainWindow>

class QAction;
class CentralWidget;

class MainWindow : public QMainWindow {

    Q_OBJECT

public:
	MainWindow();

public:
    void closeEvent(QCloseEvent* event);

private:
    void createActions();
    void createMenu();
    void createToolBar();
    void createStatusBar();
    
    void readSettings();
        
private:
    CentralWidget* myCentralWidget;

    QAction* myExitAction;
    QAction* myFullScreenAction;
    QAction* mySettinsAction;
};

#endif //_MAIN_WINDOW_H_
