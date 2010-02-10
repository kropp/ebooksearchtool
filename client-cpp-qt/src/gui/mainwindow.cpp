#include <QtGui>

#include "mainwindow.h"
#include "centralwidget.h"
#include "searchwidget.h"
#include "dialogs/internetConnectionDialog.h"

MainWindow::MainWindow() {
    mySearchWidget = new SearchWidget(this);
    myCentralWidget = new CentralWidget(this);
    createActions();
    createMenu();
    createToolBar();
    createStatusBar();
    readSettings();
	setCentralWidget(myCentralWidget);

    myInternetConnectionDialog = new InternetConnectionDialog(this);

    connect(mySearchWidget, SIGNAL(search(QString)), myCentralWidget, SLOT(downloadFile(QString))); 
    connect(mySearchWidget, SIGNAL(search(QString)), this, SLOT(search(QString))); 
    connect(myCentralWidget, SIGNAL(stateChanged(QString)), this, SLOT(updateStatusLabel(QString))); 

    const NetworkManager* nManager = myCentralWidget->getNetworkManager();
    connect(nManager, SIGNAL(dataReadProgress(int, int)),
            this, SLOT(updateProgressBar(int, int)));
    
//    connect(mySearchWidget, SIGNAL(search(QString)), this, updateStatusBar());

    mySearchWidget->setFocus();
    setWindowTitle("ebooksearchtool");
//	showMaximized();
}

void MainWindow::closeEvent(QCloseEvent* event) {
    writeSettings();
    event->accept();
}

void MainWindow::createActions() {
    myExitAction = new QAction(tr("E&xit"), this);
    myExitAction->setShortcut(tr("Ctrl+X"));
    myExitAction->setStatusTip(tr("Exit the application"));
    
    mySetConnectionAction = new QAction(tr("Internet Connection..."), this);
    mySetConnectionAction->setStatusTip(tr("Set internet connection parameters"));
    connect(mySetConnectionAction, SIGNAL(triggered()), 
            this, SLOT(showInternetConnectionDialog()));

    myFullScreenAction = new QAction(tr("Full screen"), this);
    
    myBackAction = new QAction(tr("Back"), this);
    myBackAction->setIcon(QIcon("view/images/back.png"));

    myStopAction = new QAction(tr("Stop"), this);
    myStopAction->setIcon(QIcon("view/images/stop.jpeg"));
}

void MainWindow::createMenu() {
    QMenu* fileMenu = menuBar()->addMenu(tr("File"));
    fileMenu->addAction(myExitAction);
    
    QMenu* viewMenu = menuBar()->addMenu(tr("View"));
    viewMenu->addAction(myFullScreenAction);
    viewMenu->addAction(myStopAction);
    
    QMenu* editMenu = menuBar()->addMenu(tr("Settings"));
    editMenu->addAction(mySetConnectionAction);
    
    QMenu* historyMenu = menuBar()->addMenu(tr("History"));
    historyMenu->addAction(myBackAction);
}

void MainWindow::showInternetConnectionDialog() {
    myInternetConnectionDialog->show();
}

void MainWindow::createToolBar() {
    QToolBar* viewToolBar = addToolBar(tr("View"));
   // viewToolBar->addAction(myBackAction);
   // viewToolBar->addAction(myStopAction);
    viewToolBar->addWidget(mySearchWidget);
}

void MainWindow::createStatusBar() {
    // create Status Label
    myStatusLabel = new QLabel("");
    myStatusLabel->setAlignment(Qt::AlignLeft);
    statusBar()->addWidget(myStatusLabel);
    // create Progress Bar
    createProgressBar();
    myProgressBar->setAlignment(Qt::AlignRight);
    statusBar()->addWidget(myProgressBar);
}

void MainWindow::updateStatusBar() {
    // label <- search что ищем
    // if (поиск завершен)
    // hide progress bar
    // label <- найдено, показано
}

void MainWindow::updateProgressBar(int done, int total) {
    int progress = done * 100/ total;
    myProgressBar->show();
    myProgressBar->setValue(progress);
    if (progress == 100) {
        myProgressBar->hide();
    }
}

void MainWindow::updateStatusLabel(const QString& message) {
    //QString status = tr("Search: ");
    myStatusLabel->setText(message);
}

void MainWindow::readSettings() {
    qDebug() << "MainWindow::readSettings";
    QSettings settings(View::ourConfigFilePath, QSettings::IniFormat);
    resize(settings.value("mainwindow/size").toSize());
    if (settings.value("mainwindow/fullScreen").toBool()) {
        showFullScreen();
    }
}
    
void MainWindow::writeSettings() {
    QSettings settings(View::ourConfigFilePath, QSettings::IniFormat);
    settings.beginGroup("mainwindow");
    settings.setValue("size", this->size());
    settings.setValue("fullScreen", this->isFullScreen());
    settings.endGroup();
}

void MainWindow::search(const QString& query) {
    QString message(query);
    updateStatusLabel(message.prepend(tr("Searching: ")));
}

void MainWindow::createProgressBar() {
    myProgressBar = new QProgressBar();
    myProgressBar->setRange(0, 100);
    myProgressBar->setValue(0);
    myProgressBar->hide();
}
