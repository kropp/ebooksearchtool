#include "bookActionButtons.h"

BookActionsButtonBox::BookActionsButtonBox(QWidget* parent) : QGroupBox(parent) {
    QHBoxLayout* buttonLayout = new QHBoxLayout();
    createButtons(buttonLayout);
    setLayout(buttonLayout);
    setFlat(true);
    connect(myToLibraryButton, SIGNAL(pressed()), this, SIGNAL(toLibrary()));
    connect(myDownloadButton, SIGNAL(pressed()), this, SIGNAL(download()));
    connect(myReadButton, SIGNAL(pressed()), this, SIGNAL(read()));
}

void BookActionsButtonBox::createButtons(QHBoxLayout* layout) {
    QIcon* toLibraryIcon = new QIcon("view/images/book_add.png");
    myToLibraryButton = new QPushButton(*toLibraryIcon, " ");
    myToLibraryButton->setToolTip("add to library");
    
    QIcon* downloadIcon = new QIcon("view/images/download.png");
    myDownloadButton = new QPushButton(*downloadIcon, "");
    myDownloadButton->setToolTip("download");

    QIcon* readIcon = new QIcon("view/images/book_open.png");
    myReadButton = new QPushButton(*readIcon, " ");
    myReadButton->setToolTip("read");

    applyButtonSettings(myToLibraryButton);    
    applyButtonSettings(myDownloadButton);    
    applyButtonSettings(myReadButton);    

    layout->addWidget(myToLibraryButton);
    layout->addWidget(myDownloadButton);
    layout->addWidget(myReadButton);

    layout->setSpacing(1);  
}

void BookActionsButtonBox::applyButtonSettings(QPushButton* button) const {
    button->resize(button->iconSize());
    button->setIconSize(QSize(30, 30));
    button->setFixedSize(QSize(30, 30));
    button->setFlat(true);
    button->setCursor(Qt::PointingHandCursor);
}
