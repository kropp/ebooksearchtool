#include "view.h"
#include "bookwidget.h"
#include <QLabel>

View::View(QWidget* parent, Data* data) : QWidget(parent), myData(data) {
    myLayout = new QVBoxLayout();
    setLayout(myLayout);
}

void View::setData(Data* data) {
    myData = data;
	repaint();
}

void View::update() {
// рисуем те книги, которые добавились в модель
    if (myData != 0) {
        size_t size = myData->getSize();
        for (size_t i = myLayout->count(); i < size; ++i) {
            BookWidget* widget = new BookWidget(this, myData->getBook(i));
            myLayout->addWidget(widget);
            widget->show();
        }
    }
}

void View::repaint() {
//remove all Widgets from layout
    const int count = myLayout->count();
    for (size_t i = 0; i < count; ++i) {
        myLayout->removeItem(myLayout->itemAt(0));    
    }    
//add new Widgets
    if (myData != 0) {
        size_t size = myData->getSize();
        for (size_t i = 0; i < size; ++i) {
            BookWidget* widget = new BookWidget(this, myData->getBook(i));
            myLayout->addWidget(widget);
            widget->show();
        }
    }
}

