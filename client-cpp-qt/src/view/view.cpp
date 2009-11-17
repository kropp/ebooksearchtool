#include "view.h"
#include "bookwidget.h"
#include <QLabel>
#include <QDebug>

View::View(QWidget* parent, Data* data) : QWidget(parent), myData(data) { 
    myBooksLayout = new QVBoxLayout();
    setLayout(myBooksLayout);
}

void View::setData(Data* data) {
    myData = data;
}

void View::update() {
    clear();
    if (myData != 0) {
        makeHeader();
        const size_t size = (myData->getSize() < 5) ? myData->getSize() : 5;
        for (size_t i = 0; i < size; ++i) {
            BookWidget* widget = new BookWidget(this, myData->getBook(i));
            myBooksLayout->addWidget(widget);
            widget->show();
        }
    }
}

void View::clear() {
// remove all widgets from layout
    const size_t count = myBooksLayout->count();
    for (size_t i = 0; i < count; ++i) {
        myBooksLayout->removeWidget((BookWidget*)myBooksLayout->itemAt(0));    
    }    
}

/*void View::repaint() {
//remove all Widgets from layout
//add new Widgets
    if (myData != 0) {
        size_t size = myData->getSize();
        for (size_t i = 0; i < size; ++i) {
            BookWidget* widget = new BookWidget(this, myData->getBook(i));
            myBooksLayout->addWidget(widget);
            widget->show();
        }
    }
}*/

void View::makeHeader() const {
    //QHBoxLayout* upLayout = new QHBoxLayout();
    //upLayout->addWidget();
}
