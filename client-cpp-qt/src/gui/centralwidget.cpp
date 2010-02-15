#include <QtGui>

#include "centralwidget.h"
#include "../opds_parser/parser.h"
#include "../network/networkmanager.h"
#include "../data/data.h"
#include "searchwidget.h"

CentralWidget::CentralWidget(QWidget* parent) : QWidget(parent), myBuffer(0), myData(0) {
    myNewRequest = true;
    myView = new View(this, 0);
    myScrollArea = new QScrollArea(this);
//    myScrollArea->setWidget(myView);
  //  myView->show();
    
    myNetworkManager = NetworkManager::getInstance(); 

	connect(myNetworkManager, SIGNAL(requestFinished(int, bool)), this, SLOT(httpRequestFinished(int, bool)));
    connect(myView, SIGNAL(stateChanged(QString)), this, SIGNAL(stateChanged(QString)));
	//connect(myView, SIGNAL(urlRequest(const QString&)), this, SLOT(downloadFile(const QString&)));

	QVBoxLayout *mainLayout = new QVBoxLayout();
	mainLayout->addWidget(myScrollArea);
	setLayout(mainLayout);
}

CentralWidget::~CentralWidget() {
    delete myNetworkManager;
}

void CentralWidget::downloadFile(const QString& url) {
//    qDebug() << "CentralWidget::downloadFile " << query;
    myNewRequest = true;
	
	if (!myBuffer) {
        myBuffer = new QBuffer();
    } else {   
        myBuffer->setData("", 0);	
    }
    
    qDebug() << "CentralWidget::downloadFile " << url;
	myRequestId = myNetworkManager->download(url, myBuffer);
  
    if (myBuffer->isOpen()) {
        myBuffer->close();
    }
}

void CentralWidget::httpRequestFinished(int requestId , bool) {
    if (requestId != myRequestId) {
        return;
    }
    //qDebug() << "CentralWidget::httpRequestFinished give buffer to parser";
	parseDownloadedFile();
}

void CentralWidget::parseDownloadedFile() {
// TEMPORARY - to look in fild
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
	    myView->setData((Data*)myData);
    }
    myBuffer->open(QIODevice::ReadOnly);
    parser.parse(myBuffer, myView->getData());
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

const NetworkManager* CentralWidget::getNetworkManager() const {
    return myNetworkManager;
}
