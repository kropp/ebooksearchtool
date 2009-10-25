#ifndef _MAIN_WINDOW_H
#define _MAIN_WINDOW_H

#include <QDialog>

#include "../network/httpconnection.h"
#include "../view/view.h" 

class QDialogButtonBox;
class QFile;
class QComboBox;
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
	~MainWindow(); // TODO в деструкторе сохранять все настройки
 
private slots:
	void downloadFile();
	void downloadFile(const QString& url);
	void httpRequestFinished(int requestId, bool error);
	void enableSearchButton();
	void parseDownloadedFile();

private:
	QString queryToUrl() const;

private:
	QLabel *myStatusLabel;
	QLabel *myUrlLabel;
	QLineEdit *myUrlLineEdit;
	QLineEdit *myQueryLineEdit;
	QComboBox  *mySearchTags;
	QPushButton *mySearchButton;
	QDialogButtonBox *myButtonBox;

	HttpConnection* myHttpConnection;
	QFile *myFile; 

	View* myView;
};

#endif //_MAIN_WINDOW_H_
