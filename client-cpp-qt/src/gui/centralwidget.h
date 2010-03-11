#ifndef _CENTRAL_WIDGET_H_
#define _CENTRAL_WIDGET_H_

#include "../view/view.h" 
#include "../data/search_result.h"

class QDialogButtonBox;
class QBuffer;
class NetworkManager;
class SearchWidget;
class ServersListView;
class QScrollArea;
class QErrorMessage;

class CentralWidget : public QWidget {
    
    Q_OBJECT    

private:
    CentralWidget(QWidget* parent);
    ~CentralWidget();
    
private slots:
	void downloadFile(const QString& query);
	void httpRequestFinished(int requestId, bool error);
	void parseDownloadedFile();
    void getNextResult();

signals:
    void stateChanged(const QString& message);
    void hasNextResult(bool);

private:
    const NetworkManager* getNetworkManager() const;
    void resizeEvent(QResizeEvent* event) const;
    void nextDownloading();
    void updateView();
//    void fillComboBox();
    void saveOldData();

private:
//    QDialogButtonBox *myButtonBox;

	QBuffer *myBuffer; 
    NetworkManager* myNetworkManager;
    int myRequestId;
    bool myNewRequest;
    bool myIsDownloadingNextResults;
    QList<QString> myUrlsForDownloading;
    // содержит дополнительные результаты поиска - кроме самих книг
    // например, ссылки на продолжение 
    SearchResult mySearchResult;

    Data* myData;
    View* myView;
    
//    ServersListView* myServersListView;
    QScrollArea* myScrollArea;
   
    QErrorMessage* myErrorMessageDialog;

friend class MainWindow;
};

#endif //_CENTRAL_WIDGET_H
