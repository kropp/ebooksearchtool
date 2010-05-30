#include "bookresultview.h"
#include "../ViewModel/bookresultviewmodel.h"
#include "../Model/settingsmanager.h"

#include <QHBoxLayout>
#include <QLabel>
#include <QPushButton>
#include <QFont>
#include <QFile>
#include <QFileDialog>
#include <QMouseEvent>
#include <QPalette>

#include <QDebug>

QColor BookResultView::ourBackgroundSelectedColor(QColor::fromHsv(200, 10, 255));
QColor BookResultView::ourBackgroundColor(Qt::white);

BookResultView::BookResultView(BookResultsView* parent, BookResultViewModel* bookResultViewModel, bool addToLibraryButton) : StandardView(parent)
{
    myParent = parent;
    myViewModel = bookResultViewModel;

    myAddToLibraryButtonEnabled = addToLibraryButton;

    initialize();
}

BookResultView::~BookResultView()
{
}

void BookResultView::bookDownloadStateChanged(QString newState)
{
    myDownloadButton->setState(newState);
    this->setStyleSheet(this->styleSheet());
}

void BookResultView::createComponents()
{
    myBookPictureLabel = new QLabel(this);
    myBookPictureLabel->setObjectName("bookPictureLabel");
    QIcon* coverIcon = myViewModel->getCoverIcon();
    if (coverIcon) {
        setCover(coverIcon);
    }

    myBookTitleLabel = new QLabel(myViewModel->getBookName());
    myBookTitleLabel->setObjectName("bookTitleLabel");
    myBookAuthorLabel = new QLabel("Author: " + myViewModel->getAuthorName());
    myBookAuthorLabel->setObjectName("bookAuthorLabel");

    myBookLanguageLabel = new QLabel("Language: " + myViewModel->getLanguage());
    myBookLanguageLabel->setObjectName("bookLanguageLabel");
    myBookServerLabel = new QLabel("Server: " + myViewModel->getServerName());
    myBookServerLabel->setObjectName("bookServerLabel");

    myDownloadButton = new MultiStateButton(this);
    myDownloadButton->setObjectName("downloadButton");

    myDownloadButton->setToolTip("Download book");

    if (myViewModel->canBeDownloaded()) {
        myDownloadButton->setState("normal");
    } else {
        myDownloadButton->setState("grayed");
        myDownloadButton->setEnabled(false);
    }
    if (myViewModel->isDownloaded()) {
        myDownloadButton->setState("downloaded");
    }

    if (myAddToLibraryButtonEnabled)
    {
        myAddToLibraryButton = new QPushButton(this);
        myAddToLibraryButton->setCursor(Qt::PointingHandCursor);
        myAddToLibraryButton->setObjectName("addToLibraryButton");
        myAddToLibraryButton->setToolTip("Add to library");
    }
    else
    {
        myRemoveFromLibraryButton = new QPushButton(this);
        myRemoveFromLibraryButton->setCursor(Qt::PointingHandCursor);
        myRemoveFromLibraryButton->setObjectName("removeFromLibraryButton");
        myRemoveFromLibraryButton->setToolTip("Remove from library");
    }

    myReadButton = new QPushButton(this);
    myReadButton->setToolTip("Read book");
    myReadButton->setCursor(Qt::PointingHandCursor);


    myReadButton->setObjectName("readButton");
}

void BookResultView::layoutComponents()
{
    QHBoxLayout* bookLineLayout = new QHBoxLayout;

    bookLineLayout->setMargin(0);
    bookLineLayout->setSpacing(-1);

    bookLineLayout->addWidget(myBookPictureLabel);
    bookLineLayout->addSpacing(10);


    QVBoxLayout* bookNameAndAuthorLayout = new QVBoxLayout;

    bookNameAndAuthorLayout->setMargin(0);
    bookNameAndAuthorLayout->setSpacing(-1);

    bookNameAndAuthorLayout->addSpacing(5);

    bookNameAndAuthorLayout->addWidget(myBookTitleLabel, 0, Qt::AlignTop);
    bookNameAndAuthorLayout->addSpacing(5);
    bookNameAndAuthorLayout->addWidget(myBookAuthorLabel);
    bookNameAndAuthorLayout->addSpacing(5);


    QHBoxLayout* languageServerLayout = new QHBoxLayout;

    languageServerLayout->addWidget(myBookLanguageLabel);
    languageServerLayout->addSpacing(5);
    languageServerLayout->addWidget(myBookServerLabel);
    languageServerLayout->addStretch(1);

    bookNameAndAuthorLayout->addItem(languageServerLayout);

    bookLineLayout->addItem(bookNameAndAuthorLayout);

    bookNameAndAuthorLayout->addStretch(1);

    bookLineLayout->addSpacing(10);


    QHBoxLayout* buttonsLayout = new QHBoxLayout;

    buttonsLayout->addStretch(1);
    buttonsLayout->addWidget(myDownloadButton);

    if (myAddToLibraryButtonEnabled)
    {
        buttonsLayout->addWidget(myAddToLibraryButton);
    }
    else
    {
        buttonsLayout->addWidget(myRemoveFromLibraryButton);
    }

    buttonsLayout->addWidget(myReadButton);
    //    buttonsLayout->addWidget(myInformationButton);

    bookLineLayout->addStretch(1);
    bookLineLayout->addItem(buttonsLayout);

    //    bookLineLayout->addWidget(myFrame);
    this->setLayout(bookLineLayout);

}

void BookResultView::setWindowParameters()
{
    QFile styleSheetFile(":/qss/BookStyle");
    styleSheetFile.open(QIODevice::ReadOnly);
    this->setStyleSheet(styleSheetFile.readAll());
    setBackgroundColor(Qt::white);
    select();
}

void BookResultView::setConnections()
{
    connect(myViewModel, SIGNAL(bookDownloadStateChanged(QString)), this, SLOT(bookDownloadStateChanged(QString)));
    //    connect(myInformationButton, SIGNAL(clicked()), this, SLOT(informationButtonPressed()));
    //    connect(myInformationButton, SIGNAL(clicked()), this, SLOT(bookInfoPressed()));
    //    connect(myInformationButton, SIGNAL(clicked()), this, SLOT(informationButtonPressed()));
    connect(myDownloadButton, SIGNAL(clicked()), this, SLOT(downloadButtonPressed()));
    connect(myReadButton, SIGNAL(clicked()), this, SLOT(readButtonPressed()));

    if (myAddToLibraryButtonEnabled)
    {
        connect(myAddToLibraryButton, SIGNAL(clicked()), this, SLOT(addToLibraryButtonPressed()));
    }
    else
    {
        connect(myRemoveFromLibraryButton, SIGNAL(clicked()), this, SLOT(removeFromLibraryButtonPressed()));
    }

    connect(myViewModel, SIGNAL(bookCoverChanged(QIcon*)), this, SLOT(setCover(QIcon*)));
}

void BookResultView::bookInfoPressed()
{
    myViewModel->bookInfoRequested();
}

void BookResultView::resizeEvent(QResizeEvent* /*event*/)
{

}

void BookResultView::downloadButtonPressed()
{
    myViewModel->downloadingRequested();


    //    QString name = myViewModel->getFileName();
    //    QString fileName(QFileDialog::getSaveFileName(0, tr("Download book"),
    //                                                  name,
    //                                                  QString("*.") + SettingsManager::getInstance()->getCurrentFormat()));
    //    qDebug() << "BookResultView::downloadButtonPressed() file name for saving " << fileName;
    //    if (fileName.isEmpty()) {
    //        return;
    //    }
    //    myViewModel->downloadingRequested(fileName);
}

void BookResultView::addToLibraryButtonPressed()
{
    myViewModel->addBookToLibraryRequested();
}

void BookResultView::removeFromLibraryButtonPressed()
{
    myViewModel->removeBookFromLibraryRequested();
}

void BookResultView::readButtonPressed()
{
    myViewModel->readRequested();
}


void BookResultView::mousePressEvent  ( QMouseEvent * ) {
    //  qDebug() << "BookResultView::mousePressEvent();";
    myParent->changeSelectedBookRequest(this);
    bookInfoPressed();
}

void BookResultView::select() {
    setBackgroundColor(ourBackgroundSelectedColor);
}

void BookResultView::cancelSelection() {
    setBackgroundColor(ourBackgroundColor);
}

void BookResultView::setCover(QIcon* icon)
{
    myBookPictureLabel->setPixmap(icon->pixmap(QSize(100,100), QIcon::Normal, QIcon::On));
}

void BookResultView::setBackgroundColor(QColor color) {
    QPalette pal = this->palette();
    pal.setColor(QPalette::Background, color);
    this->setPalette(pal);
    this->setAutoFillBackground(true);
}
