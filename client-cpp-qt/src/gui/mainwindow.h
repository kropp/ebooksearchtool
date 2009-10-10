#ifndef _MAIN_WINDOW_H
#define _MAIN_WINDOW_H

#include <QDialog>

#include "../network/httpconnection.h"
#include "../view/view.h" 

class QDialogButtonBox;
class QFile;
class QHttp;
class QHttpResponseHeader;
class QLabel;
class QLineEdit;
class QTextEdit;
class QProgressDialog;
class QPushButton;
class QAuthenticator;

class MainWindow : public QDialog {

     Q_OBJECT

public:
	MainWindow(QWidget *parent = 0);
	~MainWindow();

private slots:
	void downloadFile();
	void httpRequestFinished(int requestId, bool error);
	void enableDownloadButton();
	void parseDownloadedFile();

private:
	QLabel *myStatusLabel;
	QLabel *myUrlLabel;
	QLineEdit *myUrlLineEdit;
	QPushButton *myDownloadButton;
	QPushButton *myQuitButton;
	QDialogButtonBox *myButtonBox;

	HttpConnection* myHttpConnection;
	QFile *myFile; 

	View* myView;
};

#endif //_MAIN_WINDOW_H_
