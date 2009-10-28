#include <QtGui>

#include "centralwidget.h"

CentralWidget::CentralWidget(QWidget* parent) : QWidget(parent), myFile(0) {
    myNewRequest = true;
	myUrlLineEdit = new QLineEdit("http://");
	myUrlLabel = new QLabel(tr("URL:"));
	myQueryLineEdit = new QLineEdit();
    mySearchTags = new QComboBox(this);
    mySearchTags->addItem("title");
    mySearchTags->addItem("author");
    mySearchTags->addItem("general");
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
	//showMaximized();
}

