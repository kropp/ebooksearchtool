#include <QtGui>

#include "mainwindow.h"
#include "centralwidget.h"
#include "searchwidget.h"

MainWindow::MainWindow() {
    mySearchWidget = new SearchWidget(this);
//    mySearchWidget->resize(10, 100);
    createActions();
    createMenu();
    createToolBar();
    createStatusBar();
    readSettings();
    myCentralWidget = new CentralWidget(this);
	setCentralWidget(myCentralWidget);

    connect(mySearchWidget, SIGNAL(search(QString)), myCentralWidget, SLOT(downloadFile(QString))); 
	
    mySearchWidget->setFocus();
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
    
    myBackAction = new QAction(tr("Back"), this);
//    myBackAction->setIcon(QIcon("view/images/back.png"));

    myStopAction = new QAction(tr("Stop"), this);
  //  myStopAction->setIcon(QIcon("view/images/stop.jpeg"));
}

void MainWindow::createMenu() {
    QMenu* fileMenu = menuBar()->addMenu(tr("File"));
    fileMenu->addAction(myExitAction);
    
    QMenu* viewMenu = menuBar()->addMenu(tr("View"));
    viewMenu->addAction(myFullScreenAction);
    viewMenu->addAction(myStopAction);
    
    QMenu* editMenu = menuBar()->addMenu(tr("Edit"));
    editMenu->addAction(mySettinsAction);
    
    QMenu* historyMenu = menuBar()->addMenu(tr("History"));
    historyMenu->addAction(myBackAction);
}

void MainWindow::createToolBar() {
    QToolBar* viewToolBar = addToolBar(tr("View"));
   // viewToolBar->addAction(myBackAction);
   // viewToolBar->addAction(myStopAction);
    viewToolBar->addWidget(mySearchWidget);
}

void MainWindow::createStatusBar() {

}

void MainWindow::readSettings() {

}


