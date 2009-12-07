#include <QtGui>

#include "mainwindow.h"
#include "centralwidget.h"
#include "searchwidget.h"

MainWindow::MainWindow() {
    mySearchWidget = new SearchWidget(this);
    myCentralWidget = new CentralWidget(this);
//    mySearchWidget->setAlignment(Qt::AlignmentRight);
//    mySearchWidget->resize(10, 100);
    createActions();
    createMenu();
    createToolBar();
    createStatusBar();
    readSettings();
	setCentralWidget(myCentralWidget);

    connect(mySearchWidget, SIGNAL(search(QString)), myCentralWidget, SLOT(downloadFile(QString))); 
    
    connect(mySearchWidget, SIGNAL(search(QString)), this, SLOT(updateStatusLabel(QString))); 
    
//    connect(mySearchWidget, SIGNAL(search(QString)), this, updateStatusBar());

    mySearchWidget->setFocus();
    setWindowTitle("ebooksearchtool");
//	showMaximized();
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
    myStatusLabel = new QLabel("");
    myStatusLabel->setAlignment(Qt::AlignLeft);
    statusBar()->addWidget(myStatusLabel);

    QProgressBar* progressBar = myCentralWidget->getProgressBar();
    progressBar->setAlignment(Qt::AlignRight);
    //myProgressBar->setValue(value);
    statusBar()->addWidget(progressBar);
}

void MainWindow::updateStatusBar() {
    // label <- search что ищем
    // if (поиск завершен)
    // hide progress bar
    // label <- найдено, показано
}

void MainWindow::updateStatusLabel(const QString& query) {
    QString status = tr("Search: ");
    myStatusLabel->setText(status.append(query));
}

void MainWindow::readSettings() {

}


