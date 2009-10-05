#include <QtGui>

#include "mainwindow.h"
#include "../xml_parser/parser.h"
#include "../network/httpconnection.h"
#include "../model/model.h"

MainWindow::MainWindow(QWidget *parent) : QDialog(parent), myFile(0) {
	myUrlLineEdit = new QLineEdit("http://feedbooks.com/books/search.atom?query=");

	myUrlLabel = new QLabel(tr("&URL:"));
	myUrlLabel->setBuddy(myUrlLineEdit);
	myStatusLabel = new QLabel(tr("Please enter the URL of a file you want to "
		                     "download."));

	myDownloadButton = new QPushButton(tr("Download"));
	myDownloadButton->setDefault(true);
	myQuitButton = new QPushButton(tr("Quit"));
	myQuitButton->setAutoDefault(false);

	myButtonBox = new QDialogButtonBox();
	myButtonBox->addButton(myDownloadButton, QDialogButtonBox::ActionRole);
	myButtonBox->addButton(myQuitButton, QDialogButtonBox::RejectRole);

	myText = new QTextEdit(this);
	myText->setPlainText("You can start searching.\n");
	myText->setReadOnly(true);
	myByteArray = new QByteArray(); // TODO а это вообще уже часть модели и представления - убрать 

	myHttpConnection = new HttpConnection(this);

	connect(myUrlLineEdit, SIGNAL(textChanged(const QString &)), this, SLOT(enableDownloadButton()));
	connect(myHttpConnection, SIGNAL(requestFinished(int, bool)), this, SLOT(httpRequestFinished(int, bool)));

	connect(myDownloadButton, SIGNAL(clicked()), this, SLOT(downloadFile()));
	connect(myQuitButton, SIGNAL(clicked()), this, SLOT(close()));
	connect(myDownloadButton, SIGNAL(clicked()), this, SLOT(clearScreen()));

	QHBoxLayout *topLayout = new QHBoxLayout;
	topLayout->addWidget(myUrlLabel);
	topLayout->addWidget(myUrlLineEdit);

	QVBoxLayout *mainLayout = new QVBoxLayout;
	mainLayout->addLayout(topLayout);
	mainLayout->addWidget(myStatusLabel);
	mainLayout->addWidget(myButtonBox);
	mainLayout->addWidget(myText);
	setLayout(mainLayout);

	setWindowTitle(tr("HTTP"));
	myUrlLineEdit->setFocus();
}

void MainWindow::downloadFile() {
	//TODO - add button for setting proxy
	myHttpConnection->setProxy("192.168.0.2", 3128);
	
	if (myFile != 0) {
		delete myFile;
	}
	myFile = new QFile("downloaded");
	myFile->open(QIODevice::WriteOnly); //может и не суметь открыть
	myHttpConnection->downloadFile(myUrlLineEdit->text(), myFile);
	myDownloadButton->setEnabled(false);
}


void MainWindow::enableDownloadButton() {
	myDownloadButton->setEnabled(!myUrlLineEdit->text().isEmpty());
}

void MainWindow::httpRequestFinished(int , bool) {
	myFile->close();
	enableDownloadButton();
	parseDownloadedFile();
}

void MainWindow::parseDownloadedFile() {
	AtomParser parser;
	myFile->open(QIODevice::ReadOnly);
	//Model* model = new Model();	
	//parser.parse(myFile, model);
	myFile->close();
	myText->setPlainText(myByteArray->data());
}

void MainWindow::clearScreen() {
	myByteArray->clear();
}


