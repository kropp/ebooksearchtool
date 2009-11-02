#include <QLabel>
#include <QHBoxLayout>

#include "bookwidget.h"
#include "../data/book_author.h"

#include <iostream>

BookWidget::BookWidget(QWidget* parent, const Book* book) : QWidget(parent) ,myBook(book) {
    myTitleLable = new QLabel(myBook->getTitle().c_str());
	QHBoxLayout* layout = new QHBoxLayout();
	layout->addWidget(myTitleLable);
    //setVisible(true);
    std::cout << "widget created\n";
    setLayout(layout);
   //show();
// отображение обложки
// текст: название, автор
// свой layout
// кнопки для редактирования
//
}

//void BookWidget::paint() const {
   // show();
//}


