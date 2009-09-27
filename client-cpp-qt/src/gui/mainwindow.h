#ifndef _MAIN_WINDOW_H
#define _MAIN_WINDOW_H

#include <QDialog>

class QDialogButtonBox;
class QFile;
class QHttp;
class QHttpResponseHeader;
class QLabel;
class QLineEdit;
class QProgressDialog;
class QPushButton;
class QAuthenticator;

class HttpWindow : public QDialog {

     Q_OBJECT

public:
	HttpWindow(QWidget *parent = 0);

private slots:
	void downloadFile();
	void cancelDownload();
	void httpRequestFinished(int requestId, bool error);
	void readResponseHeader(const QHttpResponseHeader &responseHeader);
	void updateDataReadProgress(int bytesRead, int totalBytes);
	void enableDownloadButton();

private:
	QLabel *statusLabel;
	QLabel *urlLabel;
	QLineEdit *urlLineEdit;
	QProgressDialog *progressDialog;
	QPushButton *downloadButton;
	QPushButton *quitButton;
	QDialogButtonBox *buttonBox;

	QHttp *myHttp;
	QFile *myFile;
	int myHttpGetId;
	bool myHttpRequestAborted;
};

#endif //_MAIN_WINDOW_H_
