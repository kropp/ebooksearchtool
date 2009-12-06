#ifndef _CENTRAL_WIDGET_H_
#define _CENTRAL_WIDGET_H_

#include "../view/view.h" 

class QDialogButtonBox;
class QBuffer;
class NetworkManager;
class SearchWidget;

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

private:
	QString queryToUrl() const;
    void fillComboBox();

private:
//    SearchWidget* mySearchWidget;

//    QDialogButtonBox *myButtonBox;

	QBuffer *myBuffer; 
    NetworkManager* myNetworkManager;
    int myRequestId;
    bool myNewRequest;
   
    Data* myData;
	View* myView;

friend class MainWindow;
};

#endif //_CENTRAL_WIDGET_H
