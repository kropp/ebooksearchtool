#ifndef _CENTRAL_WIDGET_H_
#define _CENTRAL_WIDGET_H_

#include "../view/view.h" 

class QDialogButtonBox;
class QBuffer;
class NetworkManager;
class SearchWidget;
class QProgressBar;

class CentralWidget : public QWidget {
    
    Q_OBJECT    
    
private:
    CentralWidget(QWidget* parent);
    
private slots:
	void downloadFile(const QString& query);
	void httpRequestFinished(int requestId, bool error);
	void parseDownloadedFile();

private:
    QProgressBar* getProgressBar();
    void createProgressBar();
    QString queryToUrl(const QString& query) const;
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
    
    QProgressBar* myProgressBar;

friend class MainWindow;
};

inline QProgressBar* CentralWidget::getProgressBar() {
    return myProgressBar;
}

#endif //_CENTRAL_WIDGET_H
