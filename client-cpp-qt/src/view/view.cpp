#include "view.h"
#include "bookwidget.h"
#include <QLabel>

#include <iostream>

View::View(QWidget* parent, Data* data) : QWidget(parent), myData(data) {
    myLayout = new QVBoxLayout();
    QLabel* label = new QLabel ("VIEW");
    myLayout->addWidget(label);
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
            //QLabel* label = new QLabel(myData->getBook(i)->getTitle().c_str());
            //myLayout->addWidget(label);
            BookWidget* widget = new BookWidget(this, myData->getBook(i));
            myLayout->addWidget(widget);
            widget->show();
            std::cout << "widget added to the view\n";
        }
    }
//    show();
}

