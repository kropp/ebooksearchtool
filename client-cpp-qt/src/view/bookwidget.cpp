#include <QtGui>
//#include <QDebug>

#include "bookwidget.h"
#include "../data/book_author.h"
#include "../network/httpconnection.h"
#include "bookActionButtons.h"
#include "moreLessTextLabel.h"

BookWidget::BookWidget(QWidget* parent, const Book* book) : QWidget(parent) ,myBook(book) {
   // myHttpConnection = new HttpConnection(this);
    //connect(myHttpConnection, SIGNAL(requestFinished(int, bool)), this, SLOT(setCover()));
    //downloadCover();
   
    myCheckBox = new QCheckBox();
    QLabel* title = new QLabel(myBook->getTitle().c_str());
    QLabel* author = new QLabel(myBook->getAuthor()->getName().c_str());
    QLabel* cover = new QLabel("COVER");// попробовать любую картинку вместо обложки вставить
    MoreLessTextLabel* summary = new MoreLessTextLabel(QString::fromStdString(myBook->getSummary()).left(15),
                                                       QString::fromStdString(myBook->getSummary()), this);
    //QPalette coverPalette;
    //coverPalette.setBrush(cover->backgroundRole(), QBrush(QPixmap("view/images/book.jpeg")));
    //cover->setPalette(coverPalette);
    //cover->setAutoFillBackground(true);

    myButtonGroup = new BookActionsButtonBox(this);
    connect(myButtonGroup, SIGNAL(remove()), this, SLOT(remove()));
    connect(myButtonGroup, SIGNAL(toLibrary()), this, SLOT(toLibrary()));
    connect(myButtonGroup, SIGNAL(read()), this, SLOT(read()));

    QGridLayout* mainLayout = new QGridLayout();
    mainLayout->addWidget(myCheckBox, 0, 0, 3, 1);
    mainLayout->addWidget(cover, 0, 1, 3, 1);
    mainLayout->addWidget(title, 0, 2, Qt::AlignLeft);
    mainLayout->addWidget(author, 1, 2, Qt::AlignLeft);
    mainLayout->addWidget(myButtonGroup, 0, 3);
    mainLayout->addWidget(summary, 2, 1, 1, 4);
  
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

void BookWidget::remove() {
    emit remove(this);
}

void BookWidget::toLibrary() {
    emit toLibrary(this);
}

void BookWidget::read() {
    emit read(this);
}


void BookWidget::mark(int state) {
    myCheckBox->setCheckState(state);
}

const Book& BookWidget::getBook() const {
	return *myBook;
}
