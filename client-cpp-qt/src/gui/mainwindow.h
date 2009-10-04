#ifndef _MAIN_WINDOW_H
#define _MAIN_WINDOW_H

#include <QDialog>

#include "../network/httpconnection.h"

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

private slots:
	void downloadFile();
	void httpRequestFinished(int requestId, bool error);
	void enableDownloadButton();
	void parseDownloadedFile();
	void clearScreen();

private:
	QLabel *myStatusLabel;
	QLabel *myUrlLabel;
	QLineEdit *myUrlLineEdit;
	QTextEdit *myText;
	QPushButton *myDownloadButton;
	QPushButton *myQuitButton;
	QDialogButtonBox *myButtonBox;
	QByteArray* myByteArray;

	HttpConnection* myHttpConnection;
	QFile *myFile; 
};

#endif //_MAIN_WINDOW_H_
