#include <QLabel>
#include <QHBoxLayout>
#include <QVBoxLayout>
#include <QUrl>
#include <QFile>
#include <QFileInfo>
#include <QPushButton>
#include <QCheckBox>
#include <QButtonGroup>

#include "bookwidget.h"
#include "../data/book_author.h"
#include "../network/httpconnection.h"

#include <iostream>


BookWidget::BookWidget(QWidget* parent, const Book* book) : QWidget(parent) ,myBook(book) {
    //myHttpConnection = new HttpConnection(this);
	//connect(myHttpConnection, SIGNAL(requestFinished(int, bool)), this, SLOT(setCover()));
    //downloadCover();

    myCheckBox = new QCheckBox();
    myTitleLabel = new QLabel(myBook->getTitle().c_str());
    myAuthorLabel = new QLabel(myBook->getAuthor()->getName().c_str());
	QVBoxLayout* layout = new QVBoxLayout();
	layout->addWidget(myTitleLabel);
	layout->addWidget(myAuthorLabel);

//    myButtonGroup = new QButtonGroup();    
    QIcon* deleteIcon = new QIcon("view/images/delete.jpeg");
    QPushButton* deleteButton = new QPushButton(*deleteIcon, "", this);    
    QIcon* toLibraryIcon = new QIcon("view/images/tolibrary.jpeg");
    QPushButton* toLibraryButton = new QPushButton(*toLibraryIcon, "", this);    
    QIcon* readIcon = new QIcon("view/images/read.jpeg");
    QPushButton* readButton = new QPushButton(*readIcon, "", this);    
   // myButtonGroup->addButton(deleteButton, 1);
    //myButtonGroup->addButton(toLibraryButton, 2);
    //myButtonGroup->addButton(readButton, 3);
   
    QHBoxLayout* mainLayout = new QHBoxLayout();
    QLabel* cover = new QLabel("COVER");
//    QLabel* buttons = new QLabel("BUTTONS");
   
    mainLayout->addWidget(myCheckBox);
	mainLayout->addWidget(cover);
    mainLayout->addLayout(layout);
	//mainLayout->addWidget(buttons);
    mainLayout->addWidget(deleteButton);
    mainLayout->addWidget(toLibraryButton);
    mainLayout->addWidget(readButton);

    setLayout(mainLayout);
}

BookWidget::~BookWidget() {}

void BookWidget::downloadCover() {
/*    QString url(myBook->getCoverPath().c_str());    
	QString fileName(myBook->getTitle().c_str());
    fileName.append(".jpg");
	myFile = new QFile(fileName);
    myFile->open(QIODevice::WriteOnly);    
    
    myHttpConnection->downloadFile(url, myFile);
*/
}

void BookWidget::setCover() {
/*    std::cout << "slot: setting cover\n";    
    myCover = new QIcon(myFile->fileName());    
    myCoverButton = new QPushButton(*myCover, " ", this);
    myMainLayout->addWidget(myCoverButton);
*/
}

