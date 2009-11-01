#include "view.h"
#include "bookwidget.h"
#include <QLabel>

View::View(QWidget* parent, Data* data) : QWidget(parent), myData(data) {
    myLayout = new QVBoxLayout();
    QLabel* label = new QLabel ("VIEW");
    myLayout->addWidget(label);
    setLayout(myLayout);
}

void View::setData(Data* data) {
    myData = data;
    if (myData != 0) {
        size_t size = myData->getSize();
        for (size_t i = 0; i < size; ++i) {
            QLabel* label = new QLabel(myData->getBook(i)->getTitle().c_str());
            myLayout->addWidget(label);
        }
    }
}

void View::update() {
    if (myData != 0) {
        size_t size = myData->getSize();
        for (size_t i = 0; i < size; ++i) {
            QLabel* label = new QLabel(myData->getBook(i)->getTitle().c_str());
            myLayout->addWidget(label);
        }
    }

}

