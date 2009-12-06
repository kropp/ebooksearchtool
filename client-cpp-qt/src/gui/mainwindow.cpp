#include <QtGui>

#include "mainwindow.h"
#include "centralwidget.h"

MainWindow::MainWindow() {
    createActions();
    createMenu();
    createToolBar();
    createStatusBar();
    readSettings();
    myCentralWidget = new CentralWidget(this);
	setCentralWidget(myCentralWidget);
    setWindowTitle("ebooksearchtool");
	showMaximized();
	// set header
}

void MainWindow::closeEvent(QCloseEvent* ) {
//write settings;

}

void MainWindow::createActions() {
    myExitAction = new QAction(tr("E&xit"), this);
    myExitAction->setShortcut(tr("Ctrl+X"));
    myExitAction->setStatusTip(tr("Exit the application"));
    connect(myExitAction, SIGNAL(triggered()), qApp, SLOT(closeAllWindows()));
    
    mySettinsAction = new QAction(tr("Settings..."), this);
    myFullScreenAction = new QAction(tr("Full screen"), this);
}

void MainWindow::createMenu() {
    QMenu* fileMenu = menuBar()->addMenu(tr("File"));
    fileMenu->addAction(myExitAction);
    QMenu* viewMenu = menuBar()->addMenu(tr("View"));
    viewMenu->addAction(myFullScreenAction);
    QMenu* editMenu = menuBar()->addMenu(tr("Edit"));
    editMenu->addAction(mySettinsAction);
}

void MainWindow::createToolBar() {

}

void MainWindow::createStatusBar() {

}

void MainWindow::readSettings() {

}


