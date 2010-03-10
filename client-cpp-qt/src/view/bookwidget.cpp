#include <QtGui>
#include <QFile>
#include <QPicture>
#include <QSizePolicy>

#include "bookwidget.h"
#include "../data/book_author.h"
#include "bookActionButtons.h"
#include "bookDescriptionDialog.h"

//#include <QDebug>

static const QString COVER_DIR = "../images/";
QSize BookWidget::ourSizeHint = QSize(600, 90);

BookWidget::BookWidget(QWidget* parent, const Book* book) : QWidget(parent), myBook(book) {

// create all content
    myCheckBox = new QCheckBox();
    QString bookTitle(myBook->getTitle());
    QLabel* title = new QLabel(bookTitle.prepend("<H2>").append("</H2>"));
//    title->setWordWrap(true);
    QString authorsString;
    authorsToString(myBook->getAuthors(), authorsString);
    QLabel* author = new QLabel(authorsString);

//    QLabel* summary = makeSummary();

    BookActionsButtonBox* buttonGroup = new BookActionsButtonBox(this);

// connect buttons to actions
    connect(buttonGroup, SIGNAL(download()), this, SLOT(download()));
    connect(buttonGroup, SIGNAL(toLibrary()), this, SLOT(toLibrary()));
    connect(buttonGroup, SIGNAL(read()), this, SLOT(read()));
    connect(buttonGroup, SIGNAL(remove()), this, SLOT(remove()));
    connect(buttonGroup, SIGNAL(getInfo()), this, SLOT(showFullDescription()));

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

    setSizePolicy(QSizePolicy::Maximum, QSizePolicy::Maximum);
    //myMainLayout->setHorizontalSpacing(2);
    downloadCover();
    setBackground(); 
    setSizePolicy(QSizePolicy::Maximum, QSizePolicy::Preferred);
    setLayout(myMainLayout);
}

QSize BookWidget::sizeHint() const {
    return ourSizeHint;
}
   
void BookWidget::setWidgetWidth(size_t width) {
    ourSizeHint.setWidth(width);
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
    qDebug() << "bookWidget emit toLibrary";
    emit toLibrary(this);
}

void BookWidget::download() {

    qDebug() << "bookWidget emit download";
    emit download(this);
}


void BookWidget::read() {
    qDebug() << "bookWidget emit read";
    emit read(this);
}


void BookWidget::showFullDescription() {
    BookDescriptionDialog* dialog = new BookDescriptionDialog(this, *myBook);
    dialog->show();
   // set delete on close;
}

void BookWidget::remove() {
    qDebug() << "bookWidget emit remove";
    emit remove(this);
}

void BookWidget::mark(int state) {
    myCheckBox->setCheckState((Qt::CheckState)state);
}

void BookWidget::downloadCover() {
    const QString coverLink = myBook->getCoverLink();
    QString fileName = coverLink.right(coverLink.size() - coverLink.lastIndexOf('/') - 1);
    fileName = COVER_DIR + fileName.left(fileName.indexOf('?'));
    //if such file exists - just open it and return;
    if (QFile::exists(fileName)) {  
        myCoverFile = new QFile(fileName);
        setCover();
        return;
    }
    myCoverFile = new QFile(fileName);
    myCoverFile->open(QIODevice::WriteOnly);

    NetworkManager* manager = NetworkManager::getInstance();
    myRequestId = manager->downloadCover(coverLink, myCoverFile);
    connect(manager, SIGNAL(coverRequestFinished(int, bool)), this, SLOT(setCover(int)));  
}

QLabel* BookWidget::makeSummary() {
    QString summary = myBook->getSummary();
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

void BookWidget::authorsToString(const QVector<const Author*>& authors, QString& names) {
    names = tr("by ");
    foreach (const Author* author, authors) {
        names.append(author->getName());
        names.append("  ");
    }
}
