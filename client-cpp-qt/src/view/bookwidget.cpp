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

    QGroupBox* buttonGroup = new QGroupBox(this);
    QHBoxLayout* buttonLayout = new QHBoxLayout();
    setButtons(buttonLayout);
    buttonGroup->setLayout(buttonLayout);

    QGridLayout* mainLayout = new QGridLayout();
    mainLayout->addWidget(myCheckBox, 0, 0, 3, 1);
    mainLayout->addWidget(cover, 0, 1, 3, 1);
    mainLayout->addWidget(title, 0, 2, Qt::AlignLeft);
    mainLayout->addWidget(author, 1, 2, Qt::AlignLeft);
    mainLayout->addWidget(buttonGroup, 0, 3);
  
 //    mainLayout->setRowStretch(0, 1);
//    mainLayout->setRowStretch(1, 10);
//    mainLayout->setRowStretch(2, 10);
//    mainLayout->setRowStretch(3, 1);

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

void BookWidget::setButtons(QLayout* layout) const {
    QIcon* deleteIcon = new QIcon("view/images/delete.jpeg");
    QPushButton* deleteButton = new QPushButton(*deleteIcon, "");

    QIcon* toLibraryIcon = new QIcon("view/images/tolibrary.jpeg");
    QPushButton* toLibraryButton = new QPushButton(*toLibraryIcon, " ");

    QIcon* readIcon = new QIcon("view/images/read.jpeg");
    QPushButton* readButton = new QPushButton(*readIcon, " ");

    applyButtonSettings(toLibraryButton);    
    applyButtonSettings(deleteButton);    
    applyButtonSettings(readButton);    

    layout->addWidget(deleteButton);
    layout->addWidget(toLibraryButton);
    layout->addWidget(readButton);

    layout->setSpacing(1);  
}

void BookWidget::applyButtonSettings(QPushButton* button) const {
    button->resize(button->iconSize());
    button->setIconSize(QSize(50, 50));
    button->setFixedSize(QSize(60, 60));
    button->setFlat(true);
}
