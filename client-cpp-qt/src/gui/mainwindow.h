#ifndef _MAIN_WINDOW_H
#define _MAIN_WINDOW_H

#include <QDialog>

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

class HttpWindow : public QDialog {

     Q_OBJECT

public:
	HttpWindow(QWidget *parent = 0);
	~HttpWindow();

private slots:
	void downloadFile();
	void cancelDownload();
	void httpRequestFinished(int requestId, bool error);
	void readResponseHeader(const QHttpResponseHeader &responseHeader);
	void updateDataReadProgress(int bytesRead, int totalBytes);
	void enableDownloadButton();
	void parseDownloadedFile();

private:
	QLabel *myStatusLabel;
	QLabel *myUrlLabel;
	QLineEdit *myUrlLineEdit;
	QTextEdit *myText;
	QProgressDialog *myProgressDialog;
	QPushButton *myDownloadButton;
	QPushButton *myQuitButton;
	QDialogButtonBox *myButtonBox;

	QHttp *myHttp;
	QFile *myFile; //может, он и не нужен
	int myHttpGetId;
	bool myHttpRequestAborted;
};

#endif //_MAIN_WINDOW_H_
