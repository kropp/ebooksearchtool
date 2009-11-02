#include <QLabel>
#include <QHBoxLayout>
#include <QVBoxLayout>

#include "bookwidget.h"
#include "../data/book_author.h"

BookWidget::BookWidget(QWidget* parent, const Book* book) : QWidget(parent) ,myBook(book) {
    myTitleLabel = new QLabel(myBook->getTitle().c_str());
    myAuthorLabel = new QLabel(myBook->getAuthor()->getName().c_str());
	QVBoxLayout* layout = new QVBoxLayout();
	layout->addWidget(myTitleLabel);
	layout->addWidget(myAuthorLabel);

	QHBoxLayout* mainLayout = new QHBoxLayout();
    QLabel* cover = new QLabel("COVER");
    QLabel* buttons = new QLabel("BUTTONS");

	mainLayout->addWidget(cover);
    mainLayout->addLayout(layout);
	mainLayout->addWidget(buttons);
    
    setLayout(mainLayout);
// скачать обложку по ссылке 
// отображение обложки
// кнопки для редактирования
//
}

//void BookWidget::paint() const {
   // show();
//}


