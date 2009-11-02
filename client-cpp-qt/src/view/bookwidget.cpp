#include <QLabel>
#include <QGridLayout>
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
    QString summary(myBook->getTitle().c_str());
    summary.prepend("Summary: ");    
    QLabel* cover = new QLabel("COVER");

    QIcon* deleteIcon = new QIcon("view/images/delete.jpeg");
    QIcon* toLibraryIcon = new QIcon("view/images/tolibrary.jpeg");
    QIcon* readIcon = new QIcon("view/images/read.jpeg");

    QPushButton* deleteButton = new QPushButton(*deleteIcon, "", this);    
    QPushButton* toLibraryButton = new QPushButton(*toLibraryIcon, "", this);    
    QPushButton* readButton = new QPushButton(*readIcon, "", this);    
	
    QGridLayout* mainLayout = new QGridLayout();

    mainLayout->addWidget(myCheckBox, 0, 0, 3, 1);
    mainLayout->addWidget(cover, 0, 1, 3, 1);
    mainLayout->addWidget(myTitleLabel, 0, 2);
    mainLayout->addWidget(myAuthorLabel, 1, 2);
    //mainLayout->addWidget(summaryLabel, 2, 2);
    mainLayout->addWidget(deleteButton, 0, 3);
    mainLayout->addWidget(toLibraryButton, 1, 3);
    mainLayout->addWidget(readButton, 2, 3);

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

