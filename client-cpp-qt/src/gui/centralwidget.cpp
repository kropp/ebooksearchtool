#include <QtGui>

#include "centralwidget.h"
#include "../opds_parser/parser.h"
#include "../network/networkmanager.h"
#include "../data/data.h"
#include "searchwidget.h"
#include "serversListView.h"

CentralWidget::CentralWidget(QWidget* parent) : QWidget(parent), myBuffer(0), myData(0) {
    
// initialize fields
    myNewRequest = true;
    //myServersListView = new ServersListView(this);
    myView = new View(this, 0);
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
    
    qDebug() << "CentralWidget::downloadFile try to download" << url;
	myRequestId = myNetworkManager->download(url, myBuffer);
    qDebug() << "CentralWidget::downloadFile " << url << "id " << myRequestId;
  
    if (myBuffer->isOpen()) {
        myBuffer->close();
    }
}

void CentralWidget::httpRequestFinished(int requestId , bool error) {
    if (error) {
       myErrorMessageDialog->showMessage(myNetworkManager->errorString());
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
    qDebug() << "CentralWidget::parseDownloadedFile myBuffer content has written to the file";
// TEMPORARY end
    OPDSParser parser;
	if (myNewRequest) {
	    if (myData) {
            delete myData;
        }
        myData = new Data();
    } 

    delete myView;
    myView = new View(this, 0);
       
    myBuffer->open(QIODevice::ReadOnly);
    if (!parser.parse(myBuffer, myData)) {
        myErrorMessageDialog->showMessage("recieved no data from server");
    }
    myView->setData(myData);
    myBuffer->close();
    myView->update();	
    if (!myScrollArea->widget()) {
        myScrollArea->setWidget(myView);
    }
    
    //myScrollArea->update();
   // const QString* url = parser.getNextAtomPage();
    //if (url) {
     //   myNewRequest = false;
       // downloadFile(*url);
    //}
}

void CentralWidget::resizeEvent(QResizeEvent* event) const {
    BookWidget::setWidgetWidth(event->size().width());
}

const NetworkManager* CentralWidget::getNetworkManager() const {
    return myNetworkManager;
}
