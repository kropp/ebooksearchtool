#ifndef _CENTRAL_WIDGET_H_
#define _CENTRAL_WIDGET_H_

#include "../network/httpconnection.h"
#include "../view/view.h" 


class QDialogButtonBox;
class QBuffer;
class QComboBox;
class QHttp;
class QHttpResponseHeader;
class QLabel;
class QLineEdit;
class QTextEdit;
class QProgressDialog;
class QPushButton;
class QAuthenticator;


class CentralWidget : public QWidget {
    
    Q_OBJECT    
    
private:
    CentralWidget(QWidget* parent);
    
private slots:
	void downloadFile();
	void downloadFile(const QString& url);
	void httpRequestFinished(int requestId, bool error);
	void enableSearchButton();
	void parseDownloadedFile();
	void setNewRequest();

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

    bool myNewRequest;
	HttpConnection* myHttpConnection;
	QBuffer *myBuffer; 

	View* myView;

friend class MainWindow;
};

#endif //_CENTRAL_WIDGET_H
