#include <QtGui>

#include "bookwidget.h"
#include "../data/book_author.h"
#include "bookActionButtons.h"

#include <QFile>

BookWidget::BookWidget(QWidget* parent, const Book* book) : QWidget(parent), myBook(book) {
// create all content
    myCheckBox = new QCheckBox();
    myCover = new QLabel(" _________ ");
    downloadCover();
    QLabel* title = new QLabel(myBook->getTitle().c_str());
    QLabel* author = new QLabel(myBook->getAuthor()->getName().c_str());
    const MoreLessTextLabel* summary = makeSummary();
    BookActionsButtonBox* buttonGroup = new BookActionsButtonBox(this);

// connect buttons with actions
    connect(buttonGroup, SIGNAL(remove()), this, SLOT(remove()));
    connect(buttonGroup, SIGNAL(toLibrary()), this, SLOT(toLibrary()));
    connect(buttonGroup, SIGNAL(read()), this, SLOT(read()));

// set all content
    QGridLayout* mainLayout = new QGridLayout();
    mainLayout->addWidget(myCheckBox, 0, 0, 3, 1);
    mainLayout->addWidget(myCover, 0, 1, 2, 1, Qt::AlignLeft);
    mainLayout->addWidget(title, 0, 2, Qt::AlignLeft);
    mainLayout->addWidget(author, 1, 2, Qt::AlignLeft);
    mainLayout->addWidget(buttonGroup, 0, 3);
    mainLayout->addWidget(summary, 2, 1, 1, 4);
  
    setBackground(); 
    setLayout(mainLayout);
}

void BookWidget::setCover(int requestId) {
    if (myRequestId != requestId) {
        return;
    }
    myCoverFile->close();
    
    QPalette coverPalette;
    coverPalette.setBrush(myCover->backgroundRole(), QBrush(QPixmap(myCoverFile->fileName())));
    
    myCover->setPalette(coverPalette);
    myCover->setAutoFillBackground(true);
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

void BookWidget::downloadCover() {
    const QString coverLink = QString::fromStdString(myBook->getCoverLink());
    QString fileName = coverLink.right(coverLink.size() - coverLink.lastIndexOf('/') - 1);
    fileName = fileName.left(fileName.indexOf('?'));
    myCoverFile = new QFile(fileName);
    myCoverFile->open(QIODevice::WriteOnly);

    NetworkManager* connection = NetworkManager::getInstance();
    myRequestId = connection->download(coverLink, myCoverFile);
    connect(connection, SIGNAL(requestFinished(int, bool)), this, SLOT(setCover(int)));  
}

const MoreLessTextLabel* BookWidget::makeSummary() {
    QString summary = QString::fromStdString(myBook->getSummary());
    summary.prepend("Summary: ");
    QString begin = summary.left(50);
    return new MoreLessTextLabel(begin, summary, this);
}

void BookWidget::setBackground() {
    QPalette palette;
    palette.setColor(this->backgroundRole(), Qt::white);  // move to argument
    setPalette(palette);
    setAutoFillBackground(true);
}

