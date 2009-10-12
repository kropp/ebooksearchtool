#include <QtGui>

#include "mainwindow.h"
#include "../xml_parser/parser.h"
#include "../network/httpconnection.h"
#include "../model/model.h"

MainWindow::MainWindow(QWidget *parent) : QDialog(parent), myFile(0) {
	myUrlLineEdit = new QLineEdit("http://feedbooks.com");
	myUrlLabel = new QLabel(tr("URL:"));
	myQueryLineEdit = new QLineEdit();
	myStatusLabel = new QLabel(tr("Please enter a title or an author's name of the book you want to find"));

	mySearchButton = new QPushButton(tr("Search"));
	mySearchButton->setDefault(true);

	myView = new View(this);
	myHttpConnection = new HttpConnection(this);

	connect(myQueryLineEdit, SIGNAL(textChanged(const QString &)), this, SLOT(enableSearchButton()));
	connect(myHttpConnection, SIGNAL(requestFinished(int, bool)), this, SLOT(httpRequestFinished(int, bool)));

	connect(mySearchButton, SIGNAL(clicked()), this, SLOT(downloadFile())); 
	
	connect(myView, SIGNAL(urlRequest(const QString&)), myUrlLineEdit, SLOT(setText(const QString&)));
	connect(myView, SIGNAL(urlRequest(const QString&)), this, SLOT(downloadFile(const QString&)));
	
	QHBoxLayout *firstLayout = new QHBoxLayout;
	firstLayout->addWidget(myUrlLabel);
	firstLayout->addWidget(myUrlLineEdit);

	QHBoxLayout *secondLayout = new QHBoxLayout;
	secondLayout->addWidget(myQueryLineEdit);
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
	showMaximized();
}

MainWindow::~MainWindow() {
	if (myView) {
		delete myView;
	}
}

void MainWindow::downloadFile() {
	//TODO - add button for setting proxy
	myHttpConnection->setProxy("192.168.0.2", 3128);
	
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
	//TODO - add button for setting proxy
	myHttpConnection->setProxy("192.168.0.2", 3128);
	
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
	mySearchButton->setEnabled(!myUrlLineEdit->text().isEmpty());
}

void MainWindow::httpRequestFinished(int , bool) {
	myFile->close();
	enableSearchButton(); //надо бы ее и недоступной где-то делать
	if (myUrlLineEdit->text().contains("atom")) {
		parseDownloadedFile();
	} else if (myUrlLineEdit->text().contains("epub")) {
		myView->open(myFile->fileName());
	}
}

void MainWindow::parseDownloadedFile() {
	AtomParser parser;
	myFile->open(QIODevice::ReadOnly);
	Model* model = new Model();	
	parser.parse(myFile, model);
	myView->setModel(model);
	myFile->close();
}

QString MainWindow::queryToUrl() const {
	QString str("http://feedbooks.com/books/search.atom?query=");
	str.append(myQueryLineEdit->text());
	return str;
}

