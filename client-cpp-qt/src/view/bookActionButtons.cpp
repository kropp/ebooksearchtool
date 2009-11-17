#include "bookActionButtons.h"

BookActionsButtonBox::BookActionsButtonBox(QWidget* parent) : QGroupBox(parent) {
    QHBoxLayout* buttonLayout = new QHBoxLayout();
    setButtons(buttonLayout);
    setLayout(buttonLayout);
}

void BookActionsButtonBox::setButtons(QHBoxLayout* layout) const {
    QIcon* deleteIcon = new QIcon("view/images/delete.jpeg");
    QPushButton* deleteButton = new QPushButton(*deleteIcon, "");

    QIcon* toLibraryIcon = new QIcon("view/images/tolibrary.jpeg");
    QPushButton* toLibraryButton = new QPushButton(*toLibraryIcon, " ");

    QIcon* readIcon = new QIcon("view/images/read.jpeg");
    QPushButton* readButton = new QPushButton(*readIcon, " ");

    applyButtonSettings(toLibraryButton);    
    applyButtonSettings(deleteButton);    
    applyButtonSettings(readButton);    

    layout->addWidget(deleteButton);
    layout->addWidget(toLibraryButton);
    layout->addWidget(readButton);

    layout->setSpacing(1);  
}

void BookActionsButtonBox::applyButtonSettings(QPushButton* button) const {
    button->resize(button->iconSize());
    button->setIconSize(QSize(50, 50));
    button->setFixedSize(QSize(60, 60));
    button->setFlat(true);
}
