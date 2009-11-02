#include "view.h"
#include "bookwidget.h"
#include <QLabel>

View::View(QWidget* parent, Data* data) : QWidget(parent), myData(data) {
    myLayout = new QVBoxLayout();
    setLayout(myLayout);
}

void View::setData(Data* data) {
    myData = data;
	update();
}

void View::update() {
    if (myData != 0) {
        size_t size = myData->getSize();
        for (size_t i = 0; i < size; ++i) {
            BookWidget* widget = new BookWidget(this, myData->getBook(i));
            myLayout->addWidget(widget);
            widget->show();
        }
    }
//    show();
}

