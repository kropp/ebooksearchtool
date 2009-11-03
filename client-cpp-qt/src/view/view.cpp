#include "view.h"
#include "bookwidget.h"
#include <QLabel>

View::View(QWidget* parent, Data* data) : QWidget(parent), myData(data) {
    myBooksLayout = new QVBoxLayout();
    setLayout(myBooksLayout);
}

void View::setData(Data* data) {
    myData = data;
	repaint();
}

void View::update() {
// рисуем те книги, которые добавились в модель
    if (myData != 0) {
        makeHeader();
        size_t size = myData->getSize();
        for (size_t i = myBooksLayout->count(); i < size; ++i) {
            BookWidget* widget = new BookWidget(this, myData->getBook(i));
            myBooksLayout->addWidget(widget);
            widget->show();
        }
    }
}

void View::repaint() {
//remove all Widgets from layout
    const int count = myBooksLayout->count();
    for (size_t i = 0; i < count; ++i) {
        myBooksLayout->removeItem(myBooksLayout->itemAt(0));    
    }    
//add new Widgets
    if (myData != 0) {
        size_t size = myData->getSize();
        for (size_t i = 0; i < size; ++i) {
            BookWidget* widget = new BookWidget(this, myData->getBook(i));
            myBooksLayout->addWidget(widget);
            widget->show();
        }
    }
}

void View::makeHeader() const {
    //QHBoxLayout* upLayout = new QHBoxLayout();
    //upLayout->addWidget();
}
