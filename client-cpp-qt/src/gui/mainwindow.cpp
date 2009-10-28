#include <QtGui>

#include "mainwindow.h"
#include "centralwidget.h"
#include "../xml_parser/parser.h"
#include "../network/httpconnection.h"
#include "../model/model.h"

MainWindow::MainWindow() {
    myCentralWidget = new CentralWidget(this);
	setCentralWidget(myCentralWidget);
}

/*void MainWindow::downloadFile() {
	mySearchButton->setEnabled(false);
	
	if (myFile != 0) {
		delete myFile;
	}
	if (!myQueryLineEdit->text().isEmpty()) {
		myUrlLineEdit->setText(queryToUrl());
	}
	
	myFile = new QFile("downloaded.atom");
	myFile->open(QIODevice::WriteOnly); //может и не суметь открыть
	myHttpConnection->downloadFile(myUrlLineEdit->text(), myFile);
}

void MainWindow::downloadFile(const QString& url) {
	if (myFile != 0) {
		delete myFile;
	}
	QUrl qUrl(url);
	QFileInfo fileInfo(qUrl.path());
	QString fileName = fileInfo.fileName();
	myFile = new QFile(fileName);

	myFile->open(QIODevice::WriteOnly); //может и не суметь открыть
	myHttpConnection->downloadFile(url, myFile);
}


void MainWindow::enableSearchButton() {
	//mySearchButton->setEnabled(!myUrlLineEdit->text().isEmpty());
}

void MainWindow::httpRequestFinished(int , bool) {
	
	myFile->close();
	mySearchButton->setEnabled(true);
	if (myUrlLineEdit->text().contains("atom")) {
		parseDownloadedFile();
	} else if (myUrlLineEdit->text().contains("epub")) {
		myView->open(myFile->fileName());
	}
}

void MainWindow::parseDownloadedFile() {
	AtomParser parser;
	myFile->open(QIODevice::ReadOnly);
	if (myNewRequest) {
	    Model* model = new Model();
	    myView->resetModel(model);
    }
    parser.parse(myFile, myView->getModel());
    myView->update();	
	myFile->close();
    const QString* url = parser.getNextAtomPage();
    if (url) {
        myNewRequest = false;
        downloadFile(*url);
    }
}

QString MainWindow::queryToUrl() const {
	QString urlStr("http://");
    urlStr.append(myHttpConnection->getServer());
    urlStr.append("/books/search.atom?query=");
    const QString tag = mySearchTags->currentText();
    if ((tag == "author") || (tag == "title")) {
        urlStr.append(tag);
        urlStr.append(":");
    }
    QString queryStr = myQueryLineEdit->text();
    queryStr.replace(" ", "+");
    urlStr.append(queryStr);
	return urlStr;
}

void MainWindow::setNewRequest() {
    myNewRequest = true;
}*/
