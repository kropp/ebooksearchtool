#include "bookActionButtons.h"

#include <QDebug>

BookActionsButtonBox::BookActionsButtonBox(QWidget* parent) : QGroupBox(parent) {
    QHBoxLayout* buttonLayout = new QHBoxLayout();
    createButtons(buttonLayout);
    setLayout(buttonLayout);
    setFlat(true);
    connect(myToLibraryButton, SIGNAL(pressed()), this, SIGNAL(toLibrary()));
    connect(myDownloadButton, SIGNAL(pressed()), this, SIGNAL(download()));
    connect(myReadButton, SIGNAL(pressed()), this, SIGNAL(read()));
    connect(myRemoveButton, SIGNAL(pressed()), this, SIGNAL(remove()));
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
    
    QIcon* removeIcon = new QIcon("view/images/book_remove.png");
    myRemoveButton = new QPushButton(*removeIcon, " ");
    myRemoveButton->setToolTip("remove");

/*    QIcon* infoIcon = new QIcon("view/images/information.png");
    myInfoButton = new QPushButton(*infoIcon, " ");
    myInfoButton->setToolTip("extra information");
    
    applyButtonSettings(myInfoButton);    
  */
    applyButtonSettings(myRemoveButton);    
    applyButtonSettings(myToLibraryButton);    
    applyButtonSettings(myDownloadButton);    
    applyButtonSettings(myReadButton);    

   // layout->addWidget(myInfoButton);
    layout->addWidget(myRemoveButton);
    layout->addWidget(myToLibraryButton);
    layout->addWidget(myDownloadButton);
    layout->addWidget(myReadButton);

    layout->setSpacing(4);  
}

void BookActionsButtonBox::applyButtonSettings(QPushButton* button) const {
    // плохо - что размеры забиты руками
    //button->setIconSize(QSize(40, 40));
//    button->resize(button->iconSize());
//    qDebug() << "icon size " << button->iconSize(); 
    QSize size = button->iconSize();
    button->setFixedSize(size.width() + 4, size.height() + 2);
    button->setFlat(true);
    button->setCursor(Qt::PointingHandCursor);
    button->update();
//    button->adjustSize();
}

QSize BookActionsButtonBox::sizeHint() const {
    QSize size = myReadButton->iconSize();
    return QSize(size.width()*5 + 20, size.height() + 2);
}
