#include <QtGui>

#include "bookwidget.h"
#include "../data/book_author.h"
#include "bookActionButtons.h"
#include "moreLessTextLabel.h"

#include <QDebug>
#include <QFile>

BookWidget::BookWidget(QWidget* parent, const Book* book) : QWidget(parent), myBook(book) {
    myConnection = NetworkManager::getInstance();
    myFileName = QString::fromStdString(myBook->getTitle());
    myFileName.append(".jpeg");
    myFile = new QFile(myFileName);
    myDataStream = new QDataStream(myFile);
    //myBuffer->open(QIODevice::WriteOnly);
    myRequestId = myConnection->download(QString::fromStdString(myBook->getCoverLink()), myDataStream->device());
  //  qDebug() << "book widget cover link " << QString::fromStdString(myBook->getCoverLink());
    //qDebug() << "my request id " << myRequestId;
    connect(myConnection, SIGNAL(requestFinished(int, bool)), this, SLOT(setCover(int)));
    //downloadCover();
   
    myCheckBox = new QCheckBox();
    QLabel* title = new QLabel(myBook->getTitle().c_str());
    QLabel* author = new QLabel(myBook->getAuthor()->getName().c_str());
    myCover = new QLabel("COVER");// попробовать любую картинку вместо обложки вставить

    //-----------
    //setCover(1);
    //---------
    QString summary = QString::fromStdString(myBook->getSummary());
    summary.prepend("Summary: ");
    QString begin = summary.left(50);
    MoreLessTextLabel* annotation = new MoreLessTextLabel(begin, summary, this);

    myButtonGroup = new BookActionsButtonBox(this);
    connect(myButtonGroup, SIGNAL(remove()), this, SLOT(remove()));
    connect(myButtonGroup, SIGNAL(toLibrary()), this, SLOT(toLibrary()));
    connect(myButtonGroup, SIGNAL(read()), this, SLOT(read()));

    QGridLayout* mainLayout = new QGridLayout();
    mainLayout->addWidget(myCheckBox, 0, 0, 3, 1);
    mainLayout->addWidget(myCover, 0, 1, 2, 1, Qt::AlignLeft);
    mainLayout->addWidget(title, 0, 2, Qt::AlignLeft);
    mainLayout->addWidget(author, 1, 2, Qt::AlignLeft);
    mainLayout->addWidget(myButtonGroup, 0, 3);
    mainLayout->addWidget(annotation, 2, 1, 1, 4);
  
    QPalette palette;
    palette.setColor(this->backgroundRole(), Qt::white);
    setPalette(palette);
    setAutoFillBackground(true);

    setLayout(mainLayout);
// TODO отображение обложки
// разная ширина столбцов
}

BookWidget::~BookWidget() {}

void BookWidget::setCover(int requestId) {
//    qDebug() << "signal request finished accepted";    
    if (myRequestId != requestId) {
        return;
    }
///    myBuffer->close();
    qDebug() << "slot: setting cover started";    
    QPalette coverPalette;
//    coverPalette.setBrush(myCover->backgroundRole(), QBrush(QPixmap("view/images/read.jpeg")));
    coverPalette.setBrush(myCover->backgroundRole(), QBrush(QPixmap(myFileName)));
    myCover->setPalette(coverPalette);
    myCover->setAutoFillBackground(true);
    //myCover = new QIcon(myFile->fileName());    
    //myCoverButton = new QPushButton(*myCover, " ", this);
    //myMainLayout->addWidget(myCoverButton);
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

bool BookWidget::isMarked() const {
    return myCheckBox->checkState();
}


void BookWidget::mark(int state) {
    myCheckBox->setCheckState((Qt::CheckState)state);
}

const Book& BookWidget::getBook() const {
	return *myBook;
}
