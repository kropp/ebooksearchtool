#include <QtGui>
#include <QFile>
#include <QPicture>

#include "bookwidget.h"
#include "../data/book_author.h"
#include "bookActionButtons.h"


//#include <QDebug>

BookWidget::BookWidget(QWidget* parent, const Book* book) : QWidget(parent), myBook(book) {
// create all content
    //myCheckBox = new QCheckBox();
    QLabel* title = new QLabel(myBook->getTitle().c_str());
    QLabel* author = new QLabel(myBook->getAuthor()->getName().c_str());
    MoreLessTextLabel* summary = makeSummary();
    BookActionsButtonBox* buttonGroup = new BookActionsButtonBox(this);

// connect buttons with actions
    connect(buttonGroup, SIGNAL(remove()), this, SLOT(remove()));
    connect(buttonGroup, SIGNAL(toLibrary()), this, SLOT(toLibrary()));
    connect(buttonGroup, SIGNAL(read()), this, SLOT(read()));

// set all content
    myMainLayout = new QGridLayout();
   // myMainLayout->addWidget(myCheckBox, 0, 0, 3, 1);
    myMainLayout->addWidget(title, 0, 1, Qt::AlignLeft);
    myMainLayout->addWidget(author, 1, 1, Qt::AlignLeft);
    myMainLayout->addWidget(buttonGroup, 0, 2);
    myMainLayout->addWidget(summary, 2, 0, 1, 4);
 
    downloadCover();
//    setBackground(); 
    setLayout(myMainLayout);
}

void BookWidget::setCover(int requestId) {
    if (myRequestId != requestId) {
        return;
    }
    myCoverFile->close();
    setCover();
}

void BookWidget::setCover() {
/*
    QLabel* cover = new QLabel("        ", this);
    QPalette coverPalette;
    coverPalette.setBrush(cover->backgroundRole(), QBrush(QPixmap(myCoverFile->fileName())));
    cover->setScaledContents(true);
    cover->setPalette(coverPalette);
    cover->setAutoFillBackground(true);
*/

    QIcon* coverIcon = new QIcon(myCoverFile->fileName());
    QPushButton* coverButton = new QPushButton(*coverIcon, "");
    coverButton->resize(coverButton->iconSize());
    coverButton->setIconSize(QSize(60, 100));
    coverButton->setFixedSize(QSize(60, 100));
    coverButton->setFlat(true);

    myMainLayout->addWidget(coverButton, 0, 0, 2, 1, Qt::AlignLeft);
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

/*void BookWidget::mark(int state) {
    myCheckBox->setCheckState((Qt::CheckState)state);
}
*/
void BookWidget::downloadCover() {
    const QString coverLink = QString::fromStdString(myBook->getCoverLink());
    QString fileName = coverLink.right(coverLink.size() - coverLink.lastIndexOf('/') - 1);
    fileName = fileName.left(fileName.indexOf('?'));
    //if such file exists - just open it and return;
    if (QFile::exists(fileName)) {  
        myCoverFile = new QFile(fileName);
        setCover();
        return;
    }
    myCoverFile = new QFile(fileName);
    myCoverFile->open(QIODevice::WriteOnly);

    NetworkManager* connection = NetworkManager::getInstance();
    myRequestId = connection->download(coverLink, myCoverFile);
    connect(connection, SIGNAL(requestFinished(int, bool)), this, SLOT(setCover(int)));  
}

MoreLessTextLabel* BookWidget::makeSummary() {
    QString summary = QString::fromStdString(myBook->getSummary());
    summary.prepend("Summary: ");
    QString begin = summary.left(50);
    return new MoreLessTextLabel(begin, summary, this);
}

void BookWidget::setBackground() {
    QPalette palette;
    palette.setColor(this->backgroundRole(), Qt::white);  // to move color to argument
    setPalette(palette);
    setAutoFillBackground(true);
}

