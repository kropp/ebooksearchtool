#include <QLabel>

#include "bookwidget.h"
#include "../data/book_author.h"

BookWidget::BookWidget(QWidget* parent, const Book* book) : QWidget(parent), myBook(book) {
    myTitle = new QLabel(myBook->getTitle().c_str());
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


