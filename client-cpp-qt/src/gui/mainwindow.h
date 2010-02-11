#ifndef _MAIN_WINDOW_H_
#define _MAIN_WINDOW_H_

#include <QMainWindow>

#include "./dialogs/chooseServerDialog.h"

class QLabel;
class QProgressBar;
class QAction;
class CentralWidget;
class SearchWidget;
class InternetConnectionDialog;
//class ChooseServerDialog;

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
    void createProgressBar();

    void readSettings();
    void writeSettings();

private slots:
    void updateStatusBar();
    void updateStatusLabel(const QString& );
    void updateProgressBar(int done, int total);
    void search(const QString& query);
    void showInternetConnectionDialog();
    void showChooseServerDialog();

private:
    SearchWidget* mySearchWidget;
    CentralWidget* myCentralWidget;

    QLabel* myStatusLabel;
    QProgressBar* myProgressBar;

    QAction* myExitAction;
    QAction* myFullScreenAction;
    QAction* mySetConnectionAction;
    QAction* mySetServerAction;
    QAction* myBackAction;
    QAction* myStopAction;

    InternetConnectionDialog* myInternetConnectionDialog;
    ChooseServerDialog* myChooseServerDialog;
};

#endif //_MAIN_WINDOW_H_
