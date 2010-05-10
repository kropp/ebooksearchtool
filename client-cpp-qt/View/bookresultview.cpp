#include "bookresultview.h"
#include "../ViewModel/bookresultviewmodel.h"

#include <QHBoxLayout>
#include <QLabel>
#include <QPushButton>
#include <QFont>
#include <QFile>


BookResultView::BookResultView(QWidget* parent, BookResultViewModel* bookResultViewModel, bool addToLibraryButton) : StandardView(parent)
{
    myViewModel = bookResultViewModel;

    myAddToLibraryButtonEnabled = addToLibraryButton;

    initialize();
}

BookResultView::~BookResultView()
{

}

void BookResultView::createComponents()
{
    myBookPictureLabel = new QLabel(this);
    myBookPictureLabel->setObjectName("bookPictureLabel");

    myBookTitleLabel = new QLabel(myViewModel->getBookName());
    myBookTitleLabel->setObjectName("bookTitleLabel");
    myBookAuthorLabel = new QLabel("Author: " + myViewModel->getAuthorName());
    myBookAuthorLabel->setObjectName("bookAuthorLabel");

    myBookLanguageLabel = new QLabel("Language: " + myViewModel->getLanguage());
    myBookLanguageLabel->setObjectName("bookLanguageLabel");
    myBookServerLabel = new QLabel("Server: " + myViewModel->getServerName());
    myBookServerLabel->setObjectName("bookServerLabel");

    myDownloadButton = new QPushButton(this);
    myDownloadButton->setObjectName("downloadButton");

    if (myAddToLibraryButtonEnabled)
    {
        myAddToLibraryButton = new QPushButton(this);
        myAddToLibraryButton->setObjectName("addToLibraryButton");
    }
    else
    {
        myRemoveFromLibraryButton = new QPushButton(this);
        myRemoveFromLibraryButton->setObjectName("removeFromLibraryButton");
    }

    myReadButton = new QPushButton(this);
    myInformationButton = new QPushButton(this);

    myReadButton->setObjectName("readButton");
    myInformationButton->setObjectName("informationButton");
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
    buttonsLayout->addWidget(myInformationButton);

    bookLineLayout->addStretch(1);
    bookLineLayout->addItem(buttonsLayout);

    this->setLayout(bookLineLayout);
}

void BookResultView::setWindowParameters()
{
    QFile styleSheetFile(":/qss/BookStyle");
    styleSheetFile.open(QIODevice::ReadOnly);
    this->setStyleSheet(styleSheetFile.readAll());
}

void BookResultView::setConnections()
{
    connect(myInformationButton, SIGNAL(clicked()), this, SLOT(bookInfoPressed()));

    if (myAddToLibraryButtonEnabled)
    {
        connect(myAddToLibraryButton, SIGNAL(clicked()), this, SLOT(addToLibraryButtonPressed()));
    }
    else
    {
        connect(myRemoveFromLibraryButton, SIGNAL(clicked()), this, SLOT(removeFromLibraryButtonPressed()));
    }
}

void BookResultView::bookInfoPressed()
{
    myViewModel->bookInfoRequested();
}

void BookResultView::resizeEvent(QResizeEvent* event)
{

}


void BookResultView::downloadButtonPressed()
{

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

}

void BookResultView::informationButtonPressed()
{

}
