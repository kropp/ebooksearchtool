#include "view.h"
#include "bookwidget.h"
#include <QLabel>
#include <QDebug>

View::View(QWidget* parent, Data* data) : QWidget(parent), myData(data), myBooksNumber(0) { 
    myBooksLayout = new QVBoxLayout();
    setLayout(myBooksLayout);
}

void View::setData(Data* data) {
    if (!myData) {
       makeHeader();
		}
		myData = data;
}

void View::update() {
    clear();
    if (myData != 0) {
        const size_t size = (myData->getSize() < 5) ? myData->getSize() : 5;
        for (size_t i = 0; i < size; ++i) {
            BookWidget* widget = new BookWidget(this, myData->getBook(i));
            myBooksLayout->addWidget(widget);
            widget->show();
        }
				myBooksNumber = size;
        updateHeader();
    }
}

void View::clear() {
// remove all widgets from layout
    const size_t count = myBooksLayout->count();
    for (size_t i = 0; i < count; ++i) {
        myBooksLayout->removeWidget((BookWidget*)myBooksLayout->itemAt(0));    
    }    
}

void View::updateHeader() {
    QString shown = myShownLabel->text();
		shown.replace(QString::number(0), QString::number(myBooksNumber));
		myShownLabel->setText(shown);
    QString found = myFoundLabel->text();
    found.replace(QString::number(0), QString::number(myData->getTotalEntries()));
    myFoundLabel->setText(found);
}

void View::makeHeader() {
    myHeaderLayout = new QHBoxLayout();
    BookActionsButtonBox* buttons = new BookActionsButtonBox(this);
    myHeaderLayout->addWidget(buttons);
    QString found("FOUND: 0");
    QString shown("SHOWN: 0");
    myFoundLabel = new QLabel(found);
    myShownLabel = new QLabel(shown);
    myHeaderLayout->addWidget(myFoundLabel);
    myHeaderLayout->addWidget(myShownLabel);
	  myBooksLayout->addLayout(myHeaderLayout);
}
