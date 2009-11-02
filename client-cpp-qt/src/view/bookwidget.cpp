#include <QLabel>
#include <QHBoxLayout>

#include "bookwidget.h"
#include "../data/book_author.h"

BookWidget::BookWidget(QWidget* parent, const Book* book) : QWidget(parent) ,myBook(book) {
    myTitleLabel = new QLabel(myBook->getTitle().c_str());
    myAuthorLabel = new QLabel(myBook->getAuthor()->getName().c_str());
	QHBoxLayout* layout = new QHBoxLayout();
	layout->addWidget(myTitleLabel);
	layout->addWidget(myAuthorLabel);
    setLayout(layout);
// отображение обложки
// текст: название, автор
// свой layout
// кнопки для редактирования
//
}

//void BookWidget::paint() const {
   // show();
//}


