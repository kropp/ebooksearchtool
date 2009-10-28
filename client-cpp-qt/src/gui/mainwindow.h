#ifndef _MAIN_WINDOW_H
#define _MAIN_WINDOW_H

#include <QMainWindow>

class CentralWidget;

class MainWindow : public QMainWindow {

    Q_OBJECT

public:
	MainWindow();

private:
    CentralWidget* myCentralWidget;
};

#endif //_MAIN_WINDOW_H_
