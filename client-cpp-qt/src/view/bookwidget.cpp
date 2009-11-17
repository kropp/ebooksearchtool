#include <QLabel>
#include <QGridLayout>
#include <QUrl>
#include <QFile>
#include <QFileInfo>
#include <QPushButton>
#include <QCheckBox>
#include <QGroupBox>

#include "bookwidget.h"
#include "../data/book_author.h"
#include "../network/httpconnection.h"
#include "bookActionButtons.h"

#include <iostream>


BookWidget::BookWidget(QWidget* parent, const Book* book) : QWidget(parent) ,myBook(book) {
   // myHttpConnection = new HttpConnection(this);
    //connect(myHttpConnection, SIGNAL(requestFinished(int, bool)), this, SLOT(setCover()));
    //downloadCover();
   
    myCheckBox = new QCheckBox();
    QLabel* title = new QLabel(myBook->getTitle().c_str());
    QLabel* author = new QLabel(myBook->getAuthor()->getName().c_str());
    QLabel* cover = new QLabel("COVER");// попробовать любую картинку вместо обложки вставить
    //QPalette coverPalette;
    //coverPalette.setBrush(cover->backgroundRole(), QBrush(QPixmap("view/images/book.jpeg")));
    //cover->setPalette(coverPalette);
    //cover->setAutoFillBackground(true);

    myButtonGroup = new BookActionsButtonBox(this);

    QGridLayout* mainLayout = new QGridLayout();
    mainLayout->addWidget(myCheckBox, 0, 0, 3, 1);
    mainLayout->addWidget(cover, 0, 1, 3, 1);
    mainLayout->addWidget(title, 0, 2, Qt::AlignLeft);
    mainLayout->addWidget(author, 1, 2, Qt::AlignLeft);
    mainLayout->addWidget(myButtonGroup, 0, 3);
  
    QPalette palette;
    palette.setColor(this->backgroundRole(), Qt::white);
    setPalette(palette);
    setAutoFillBackground(true);

    setLayout(mainLayout);
// TODO отображение обложки
// разная ширина столбцов
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


