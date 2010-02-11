#ifndef _CENTRAL_WIDGET_H_
#define _CENTRAL_WIDGET_H_

#include "../view/view.h" 
//#include <QSharedDataPointer>

class QDialogButtonBox;
class QBuffer;
class NetworkManager;
class SearchWidget;
class QScrollArea;

class CentralWidget : public QWidget {
    
    Q_OBJECT    
    
private:
    CentralWidget(QWidget* parent);
    ~CentralWidget();
    
private slots:
	void downloadFile(const QString& query);
	void httpRequestFinished(int requestId, bool error);
	void parseDownloadedFile();

signals:
    void stateChanged(const QString& message);

private:
    const NetworkManager* getNetworkManager() const;
//    void fillComboBox();

private:
//    SearchWidget* mySearchWidget;

//    QDialogButtonBox *myButtonBox;

	QBuffer *myBuffer; 
    NetworkManager* myNetworkManager;
    int myRequestId;
    bool myNewRequest;
   
    Data* myData;
//    QSharedDataPointer<Data> myData;
    View* myView;
    QScrollArea* myScrollArea;
    
friend class MainWindow;
};

#endif //_CENTRAL_WIDGET_H
