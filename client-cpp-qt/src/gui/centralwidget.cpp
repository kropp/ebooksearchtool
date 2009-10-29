#include <QtGui>

#include "centralwidget.h"
#include "../xml_parser/parser.h"
#include "../network/httpconnection.h"
#include "../model/model.h"


CentralWidget::CentralWidget(QWidget* parent) : QWidget(parent), myFile(0) {
    myNewRequest = true;
	myUrlLineEdit = new QLineEdit("http://");
	myUrlLabel = new QLabel(tr("URL:"));
	myQueryLineEdit = new QLineEdit();
    mySearchTags = new QComboBox(this);
    mySearchTags->addItem("general");
   mySearchTags->addItem("title");
    mySearchTags->addItem("author");
 	myStatusLabel = new QLabel(tr("Please enter a title or an author's name of the book you want to find"));

	mySearchButton = new QPushButton(tr("Search"));
	mySearchButton->setDefault(true);

	myView = new View(this, 0);
	myHttpConnection = new HttpConnection(this);
    myUrlLineEdit->insert(myHttpConnection->getServer());

	connect(myQueryLineEdit, SIGNAL(textChanged(const QString &)), this, SLOT(enableSearchButton()));
	connect(myHttpConnection, SIGNAL(requestFinished(int, bool)), this, SLOT(httpRequestFinished(int, bool)));

	connect(mySearchButton, SIGNAL(clicked()), this, SLOT(downloadFile())); 
	connect(mySearchButton, SIGNAL(clicked()), this, SLOT(setNewRequest()));
	
	connect(myView, SIGNAL(urlRequest(const QString&)), myUrlLineEdit, SLOT(setText(const QString&)));
	connect(myView, SIGNAL(urlRequest(const QString&)), this, SLOT(downloadFile(const QString&)));
	
	QHBoxLayout *firstLayout = new QHBoxLayout;
	firstLayout->addWidget(myUrlLabel);
	firstLayout->addWidget(myUrlLineEdit);

	QHBoxLayout *secondLayout = new QHBoxLayout;
	secondLayout->addWidget(myQueryLineEdit);
	secondLayout->addWidget(mySearchTags);
	secondLayout->addWidget(mySearchButton);
	
	QVBoxLayout *topLayout = new QVBoxLayout;
	topLayout->addLayout(firstLayout);
	topLayout->addLayout(secondLayout);
	topLayout->addWidget(myStatusLabel);
	
	QGridLayout *mainLayout = new QGridLayout;
	mainLayout->addLayout(topLayout, 0, 0, 1, 3);
	mainLayout->addWidget(myView, 1, 0, 15, 5);
	setLayout(mainLayout);

	setWindowTitle(tr("Search book tool"));
	myQueryLineEdit->setFocus();
}

void CentralWidget::downloadFile() {
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

void CentralWidget::downloadFile(const QString& url) {
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


void CentralWidget::enableSearchButton() {
	//mySearchButton->setEnabled(!myUrlLineEdit->text().isEmpty());
}

void CentralWidget::httpRequestFinished(int , bool) {
	
	myFile->close();
	mySearchButton->setEnabled(true);
	if (myUrlLineEdit->text().contains("atom")) {
		parseDownloadedFile();
	} else if (myUrlLineEdit->text().contains("epub")) {
		myView->open(myFile->fileName());
	}
}

void CentralWidget::parseDownloadedFile() {
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

QString CentralWidget::queryToUrl() const {
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

void CentralWidget::setNewRequest() {
    myNewRequest = true;
}
