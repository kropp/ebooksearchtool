#include <QtGui>

#include "mainwindow.h"
#include "centralwidget.h"

MainWindow::MainWindow() {
    // createActions
    // createMenu
    // createToolBar
    // createStatusBar
    myCentralWidget = new CentralWidget(this);
	setCentralWidget(myCentralWidget);
	showMaximized();
}

