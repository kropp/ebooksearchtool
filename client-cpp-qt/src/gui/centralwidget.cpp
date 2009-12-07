#include <QtGui>

#include "centralwidget.h"
#include "../xml_parser/parser.h"
#include "../network/networkmanager.h"
#include "../data/data.h"
#include "searchwidget.h"

CentralWidget::CentralWidget(QWidget* parent) : QWidget(parent), myBuffer(0), myData(0) {
    myNewRequest = true;
	myView = new View(this, 0);
	myNetworkManager = NetworkManager::getInstance(); 

	connect(myNetworkManager, SIGNAL(requestFinished(int, bool)), this, SLOT(httpRequestFinished(int, bool)));

	//connect(myView, SIGNAL(urlRequest(const QString&)), this, SLOT(downloadFile(const QString&)));

	QVBoxLayout *mainLayout = new QVBoxLayout();
	mainLayout->addWidget(myView);//, 1, 0, 15, 5);
	setLayout(mainLayout);
}

void CentralWidget::downloadFile(const QString& query) {
//    qDebug() << "CentralWidget::downloadFile " << query;
    myNewRequest = true;
	
	if (!myBuffer) {
        myBuffer = new QBuffer();
    } else {   
        myBuffer->setData("", 0);	
    }

	myRequestId = myNetworkManager->download(queryToUrl(query), myBuffer);
  //  qDebug() << "CentralWidget::downloadFile requestID" << myRequestId;

    if (myBuffer->isOpen()) {
        myBuffer->close();
    }
}

// для подкачки файла  по конкретной ссылке
//void CentralWidget::downloadFile(const QString&) {
   /* std::cout << "slot file download\n";

	if (myFile != 0) {
		delete myFile;
	}
	QUrl qUrl(url);
	QFileInfo fileInfo(qUrl.path());
	QString fileName = fileInfo.fileName();
	myFile = new QFile(fileName);

	myFile->open(QIODevice::WriteOnly); //может и не суметь открыть
	myHttpConnection->downloadFile(url, myFile);*/
//}

//void CentralWidget::downloadFile(const QString& url, QFile& file) {
	//myHttpConnection->downloadFile(url, file);
//}


void CentralWidget::httpRequestFinished(int requestId , bool) {
    if (requestId != myRequestId) {
        return;
    }
    //qDebug() << "CentralWidget::httpRequestFinished give buffer to parser";
	parseDownloadedFile();
}

void CentralWidget::parseDownloadedFile() {
	AtomParser parser;
	if (myNewRequest) {
	    if (!myData) {
            delete myData;
        }
        myData = new Data();
	    myView->setData(myData);
    }
    myBuffer->open(QIODevice::ReadOnly);
    parser.parse(myBuffer, myView->getData());
    myBuffer->close();
    myView->update();	

   // const QString* url = parser.getNextAtomPage();
    //if (url) {
     //   myNewRequest = false;
       // downloadFile(*url);
    //}
}

QString CentralWidget::queryToUrl(const QString& query) const {
	QString urlStr("http://");
    urlStr.append(myNetworkManager->getServer());
    //TODO - не руками строку вбивать - к тому же она от сервера зависит
    urlStr.append("/books/search.atom?query=");
  //  const QString tag = myComboBox->currentText();
/*    if ((tag == "author") || (tag == "title")) {
        urlStr.append(tag);
        urlStr.append(":");
    }
*/    
    QString queryStr = query;
    queryStr.replace(" ", "+");
    urlStr.append(queryStr);

    return urlStr;
}

