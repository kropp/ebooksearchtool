#include <QtGui>
#include <QtAlgorithms>
#include "centralwidget.h"
#include "../opds_parser/parser.h"
#include "../network/networkmanager.h"
#include "../data/data.h"
#include "searchwidget.h"
//#include "serversListView.h"

CentralWidget::CentralWidget(QWidget* parent) : QWidget(parent), myBuffer(0), myData(0) {
    
// initialize fields
   // process command-line argument for initialisation
   /* if (QApplication::argc() > 1) {
        QFile file(QApplication::argv[1]);
        file->open(QIODevice::ReadOnly);
        
        OPDSParser parser;
        if (!parser.parse(file, myData, mySearchResult)) {
            myErrorMessageDialog->showMessage(tr("recieved no data from server"));
        }
        file->close();

       
        readData(file);
    }*/
   
    myNewRequest = true;
    myIsDownloadingNextResults = false;
    myView = new View(this, myData);
    myScrollArea = new QScrollArea(this);
    myErrorMessageDialog = new QErrorMessage(this);
    myErrorMessageDialog->setWindowTitle("ERROR");
    myNetworkManager = NetworkManager::getInstance(); 

// connect signals and slots
	connect(myNetworkManager, SIGNAL(requestFinished(int, bool)), this, SLOT(httpRequestFinished(int, bool)));
    connect(myView, SIGNAL(stateChanged(QString)), this, SIGNAL(stateChanged(QString)));
	//connect(myView, SIGNAL(urlRequest(const QString&)), this, SLOT(downloadFile(const QString&)));

// create layout
	QVBoxLayout *mainLayout = new QVBoxLayout();
	//mainLayout->addWidget(myServersListView);
    mainLayout->addWidget(myScrollArea);
	setLayout(mainLayout);
}

CentralWidget::~CentralWidget() {
    delete myNetworkManager;
    delete myView;
    delete myScrollArea;
    delete myErrorMessageDialog;
    if (myData) {
        delete myData;
    }
}

void CentralWidget::downloadFile(const QString& url) {
//    qDebug() << "CentralWidget::downloadFile " << query;
    myNewRequest = true;
	
	if (!myBuffer) {
        myBuffer = new QBuffer();
    } else {   
        myBuffer->setData("", 0);	
    }
    
    //qDebug() << "CentralWidget::downloadFile try to download" << url;
	myRequestId = myNetworkManager->download(url, myBuffer);
    qDebug() << "CentralWidget::downloadFile " << url << "id " << myRequestId;
}

void CentralWidget::httpRequestFinished(int requestId , bool error) {
    if (myBuffer->isOpen()) {
        myBuffer->close();
    }
    if (error){
        myErrorMessageDialog->showMessage(myNetworkManager->errorString());
        return;
    }
    
    if (requestId != myRequestId) {
        return;
    }
    //qDebug() << "CentralWidget::httpRequestFinished give buffer to parser";
	parseDownloadedFile();
}

void CentralWidget::parseDownloadedFile() {
// TEMPORARY - to look in file
    QFile* file = new QFile("search_result.atom");
    file->open(QIODevice::WriteOnly);
    file->write(myBuffer->buffer());
    file->close();
   // qDebug() << "CentralWidget::parseDownloadedFile myBuffer content has written to the file";
// TEMPORARY end
   
    saveOldData();
   
    //readData(myBuffer);

    OPDSParser parser;
    myBuffer->open(QIODevice::ReadOnly);
    if (!parser.parse(myBuffer, myData, mySearchResult)) {
        myErrorMessageDialog->showMessage(tr("recieved no data from server"));
    }
    myBuffer->close();

    if (mySearchResult.hasNextResult()) {
        emit hasNextResult(true);
    }

    updateView();
    nextDownloading();
}

/*void CentralWidget::resizeEvent(QResizeEvent* event) const {
    BookWidget::setWidgetWidth(event->size().width());
}
*/
const NetworkManager* CentralWidget::getNetworkManager() const {
    return myNetworkManager;
}
    
void CentralWidget::getNextResult() {
    qDebug() << "CentralWidget::getNextResult slot";
    mySearchResult.getLinks(myUrlsForDownloading);
    qDebug() << "CentralWidget::getNextResult urls: " << myUrlsForDownloading;
     if (myUrlsForDownloading.isEmpty()) {
        myIsDownloadingNextResults = false;
        emit hasNextResult(false);
        return;
    }
    myIsDownloadingNextResults = true;
    myNewRequest = true;
    QString url = myUrlsForDownloading.first();
    myUrlsForDownloading.pop_front();
    myRequestId = myNetworkManager->downloadByUrl(url, myBuffer); 
}

void CentralWidget::nextDownloading() {
    // if I has next result pages urls
    if (myIsDownloadingNextResults) {
        myNewRequest = false;
        qDebug() << "CentralWidget::nextDownloading " ;
        if (myUrlsForDownloading.isEmpty()) {
            myIsDownloadingNextResults = false;
            return;
        }
        QString url = myUrlsForDownloading.first();
        myUrlsForDownloading.pop_front();
        myRequestId = myNetworkManager->downloadByUrl(url, myBuffer); 
   
   // searching on the next servers
    } else if (myNetworkManager->setNextServer()) {
        myNewRequest = false;
        myRequestId = myNetworkManager->repeatDownloading(myBuffer);
    }
}

void CentralWidget::updateView() {
    myView->setData(myData);
    myBuffer->close();
    myView->update();	
    if (!myScrollArea->widget()) {
        myScrollArea->setWidget(myView);
    }
 }
 
 void CentralWidget::saveOldData() {
   if (myNewRequest) {
        if (myData) {
            delete myData;
        }
        myData = new Data();
    } 
}
    
void CentralWidget::sortResult() {
   QList<const Book*> sortedData(myData->getBooks());
   qSort(sortedData.begin(), sortedData.end(), Book::compareTitles);
   myView->showBooks(sortedData);
}
