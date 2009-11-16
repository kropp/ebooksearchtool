#include "view.h"
#include "bookwidget.h"
#include <QLabel>

View::View(QWidget* parent, Data* data) : QWidget(parent), myData(data) {
    myBooksLayout = new QVBoxLayout();
    setLayout(myBooksLayout);
}

void View::setData(Data* data) {
    myData = data;
}

void View::update() {
// рисуем те книги, которые добавились в модель
    clear();
    if (myData != 0) {
        makeHeader();
        size_t size = myData->getSize();
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

//void View::display() const {

//}


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
