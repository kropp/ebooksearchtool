#include "bookActionButtons.h"

BookActionsButtonBox::BookActionsButtonBox(QWidget* parent) : QGroupBox(parent) {
    QHBoxLayout* buttonLayout = new QHBoxLayout();
    createButtons(buttonLayout);
    setLayout(buttonLayout);
    setFlat(true);
    connect(myDeleteButton, SIGNAL(pressed()), this, SIGNAL(remove()));
    connect(myToLibraryButton, SIGNAL(pressed()), this, SIGNAL(toLibrary()));
    connect(myReadButton, SIGNAL(pressed()), this, SIGNAL(read()));
}

void BookActionsButtonBox::createButtons(QHBoxLayout* layout) {
    QIcon* deleteIcon = new QIcon("view/images/book_delete.png");
    myDeleteButton = new QPushButton(*deleteIcon, "");
    myDeleteButton->setToolTip("delete");

    QIcon* toLibraryIcon = new QIcon("view/images/book_add.png");
    myToLibraryButton = new QPushButton(*toLibraryIcon, " ");
    myToLibraryButton->setToolTip("add to library");

    QIcon* readIcon = new QIcon("view/images/book_open.png");
    myReadButton = new QPushButton(*readIcon, " ");
    myReadButton->setToolTip("read");

    applyButtonSettings(myToLibraryButton);    
    applyButtonSettings(myDeleteButton);    
    applyButtonSettings(myReadButton);    

    layout->addWidget(myDeleteButton);
    layout->addWidget(myToLibraryButton);
    layout->addWidget(myReadButton);

    layout->setSpacing(1);  
}

void BookActionsButtonBox::applyButtonSettings(QPushButton* button) const {
    button->resize(button->iconSize());
    button->setIconSize(QSize(30, 30));
    button->setFixedSize(QSize(30, 30));
    button->setFlat(true);
}
