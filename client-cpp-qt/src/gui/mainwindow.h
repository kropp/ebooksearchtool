#ifndef _MAIN_WINDOW_H
#define _MAIN_WINDOW_H

#include <QMainWindow>

class QLabel;
class QProgressBar;
class QAction;
class CentralWidget;
class SearchWidget;
class InternetConnectionDialog;

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
    void writeSettings();

private slots:
    void updateStatusBar();
    void updateStatusLabel(const QString& );
    void search(const QString& query);
    void showInternetConnectionDialog();

private:
    SearchWidget* mySearchWidget;
    CentralWidget* myCentralWidget;

    QLabel* myStatusLabel;
//    QProgressBar* myProgressBar;

    QAction* myExitAction;
    QAction* myFullScreenAction;
    QAction* mySetConnectionAction;
    QAction* myBackAction;
    QAction* myStopAction;

    InternetConnectionDialog* myInternetConnectionDialog;
};

#endif //_MAIN_WINDOW_H_
