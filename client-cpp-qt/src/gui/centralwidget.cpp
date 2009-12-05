#include <QtGui>

#include "centralwidget.h"
#include "../xml_parser/parser.h"
#include "../network/networkmanager.h"
#include "../data/data.h"

CentralWidget::CentralWidget(QWidget* parent) : QWidget(parent), myBuffer(0), myData(0) {
    myNewRequest = true;
	myQueryLineEdit = new QLineEdit(this);
    myComboBox = new QComboBox(this);
    fillComboBox();
	mySearchButton = new QPushButton(tr("Search"), this);
	mySearchButton->setDefault(true);

	myView = new View(this, 0);

	myNetworkManager = NetworkManager::getInstance(); // а может мне соединение не понадобится - отложить создание!

	connect(myQueryLineEdit, SIGNAL(textChanged(const QString &)), this, SLOT(enableSearchButton()));
	connect(myNetworkManager, SIGNAL(requestFinished(int, bool)), this, SLOT(httpRequestFinished(int, bool)));

	connect(mySearchButton, SIGNAL(clicked()), this, SLOT(downloadFile())); 
	//connect(myView, SIGNAL(urlRequest(const QString&)), this, SLOT(downloadFile(const QString&)));

	QHBoxLayout *topLayout = new QHBoxLayout;
	topLayout->addWidget(myQueryLineEdit);
	topLayout->addWidget(myComboBox);
	topLayout->addWidget(mySearchButton);
	
	QGridLayout *mainLayout = new QGridLayout;
	mainLayout->addLayout(topLayout, 0, 0, 1, 3);
	mainLayout->addWidget(myView, 1, 0, 15, 5);
	setLayout(mainLayout);

	myQueryLineEdit->setFocus();
}

void CentralWidget::downloadFile() {
    myNewRequest = true;
	//mySearchButton->setEnabled(false);
	
	if (!myBuffer) {
        myBuffer = new QBuffer();
    } else {   
        myBuffer->setData("", 0);	
    }

	myRequestId = myNetworkManager->download(queryToUrl(), myBuffer);
    myBuffer->close();
}

void CentralWidget::downloadFile(const QString&) {
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
}

//void CentralWidget::downloadFile(const QString& url, QFile& file) {
	//myHttpConnection->downloadFile(url, file);
//}

void CentralWidget::enableSearchButton() {
	//mySearchButton->setEnabled(!myUrlLineEdit->text().isEmpty());
}

void CentralWidget::httpRequestFinished(int requestId , bool) {
    if (requestId != myRequestId) {
        return;
    }
	mySearchButton->setEnabled(true);
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

QString CentralWidget::queryToUrl() const {
	QString urlStr("http://");
    urlStr.append(myNetworkManager->getServer());
    urlStr.append("/books/search.atom?query=");
    const QString tag = myComboBox->currentText();
    if ((tag == "author") || (tag == "title")) {
        urlStr.append(tag);
        urlStr.append(":");
    }
    QString queryStr = myQueryLineEdit->text();
    queryStr.replace(" ", "+");
    urlStr.append(queryStr);
	return urlStr;
}

void CentralWidget::fillComboBox() {
    myComboBox->addItem("everywhere");
    myComboBox->addItem("by title");
    myComboBox->addItem("by author");
}
