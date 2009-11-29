#include <QtGui>

#include "bookwidget.h"
#include "../data/book_author.h"
#include "bookActionButtons.h"
#include "moreLessTextLabel.h"

#include <QFile>

BookWidget::BookWidget(QWidget* parent, const Book* book) : QWidget(parent), myBook(book) {
    myConnection = NetworkManager::getInstance();
        
    const QString coverLink = QString::fromStdString(myBook->getCoverLink());
    QString fileName = coverLink.right(coverLink.size() - coverLink.lastIndexOf('/') - 1);
    fileName = fileName.left(fileName.indexOf('?'));
    myFile = new QFile(fileName);
    myFile->open(QIODevice::WriteOnly);

    myRequestId = myConnection->download(coverLink, myFile);
    connect(myConnection, SIGNAL(requestFinished(int, bool)), this, SLOT(setCover(int)));
   
    myCheckBox = new QCheckBox();
    QLabel* title = new QLabel(myBook->getTitle().c_str());
    QLabel* author = new QLabel(myBook->getAuthor()->getName().c_str());
    myCover = new QLabel("  _______ ");
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
}

void BookWidget::setCover(int requestId) {
    qDebug() << "signal request finished accepted " << requestId;    
    if (myRequestId != requestId) {
        return;
    }
    myFile->close();
    
    qDebug() << "slot: setting cover started";    
    QPalette coverPalette;
    coverPalette.setBrush(myCover->backgroundRole(), QBrush(QPixmap(myFile->fileName())));
    
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
