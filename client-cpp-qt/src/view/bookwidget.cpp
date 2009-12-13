#include <QtGui>
#include <QFile>
#include <QPicture>

#include "bookwidget.h"
#include "../data/book_author.h"
#include "bookActionButtons.h"


//#include <QDebug>

const QSize BookWidget::ourSizeHint = QSize(300, 60);

BookWidget::BookWidget(QWidget* parent, const Book* book) : QWidget(parent), myBook(book) {

// create all content
    myCheckBox = new QCheckBox();
    QLabel* title = new QLabel(QString::fromStdString(myBook->getTitle()).prepend("<H2>").append("</H2>"));
    QLabel* author = new QLabel(QString::fromStdString(myBook->getAuthor()->getName()));
//    QLabel* summary = makeSummary();

    BookActionsButtonBox* buttonGroup = new BookActionsButtonBox(this);

// connect buttons to actions
    connect(buttonGroup, SIGNAL(download()), this, SLOT(download()));
    connect(buttonGroup, SIGNAL(toLibrary()), this, SLOT(toLibrary()));
    connect(buttonGroup, SIGNAL(read()), this, SLOT(read()));
    connect(buttonGroup, SIGNAL(remove()), this, SLOT(remove()));

// set all content to layout
    myMainLayout = new QGridLayout();
    myMainLayout->addWidget(title, 0, 1, Qt::AlignLeft);
    myMainLayout->addWidget(author, 1, 1, Qt::AlignLeft);
  //  myMainLayout->addWidget(summary, 2, 1);
    myMainLayout->addWidget(buttonGroup, 0, 2, 2, 1, Qt::AlignLeft);
    myMainLayout->addWidget(myCheckBox, 0, 3, 2, 1, Qt::AlignCenter);
   
//layout settings   
    myMainLayout->setColumnStretch(0, 1);
    myMainLayout->setColumnStretch(1, 4);
    myMainLayout->setColumnStretch(2, 2);

    myMainLayout->setRowStretch(0, 1);
    myMainLayout->setRowStretch(1, 1);
    myMainLayout->setRowStretch(2, 3);

    //myMainLayout->setHorizontalSpacing(2);
    downloadCover();
    setBackground(); 
    setLayout(myMainLayout);
}

QSize BookWidget::sizeHint() const {
    return ourSizeHint;
}

void BookWidget::setCover(int requestId) {
    if (myRequestId != requestId) {
        return;
    }
    myCoverFile->close();
    setCover();
}

void BookWidget::setCover() {
    QIcon* coverIcon = new QIcon(myCoverFile->fileName());
    QPushButton* coverButton = new QPushButton(*coverIcon, "");
    coverButton->resize(coverButton->iconSize());
    coverButton->setIconSize(QSize(50, 70));
    coverButton->setFixedSize(QSize(50, 70));
    coverButton->setFlat(true);

    myMainLayout->addWidget(coverButton, 0, 0, 2, 1, Qt::AlignLeft);
}

void BookWidget::toLibrary() {
    emit toLibrary(this);
}

void BookWidget::download() {
    emit download(this);
}

void BookWidget::read() {
    emit read(this);
}

void BookWidget::remove() {
    emit remove(this);
}

void BookWidget::mark(int state) {
    myCheckBox->setCheckState((Qt::CheckState)state);
}

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

QLabel* BookWidget::makeSummary() {
    QString summary = QString::fromStdString(myBook->getSummary());
    QLabel* summaryLabel = new QLabel(summary.prepend(tr("Summary: ")));
    summaryLabel->setTextFormat(Qt::RichText);
    summaryLabel->setWordWrap(true);
    // TODO еще задать выравнивание строк по ширине
    return summaryLabel;
   // QString begin = summary.left(50);
   // return new MoreLessTextLabel(begin, summary, this);
}

void BookWidget::setBackground() {
    QPalette palette;
    palette.setColor(this->backgroundRole(), Qt::white);  // to move color to argument
    setPalette(palette);
    setAutoFillBackground(true);
}

