#include "view.h"

#include <QTextEdit>

View::View(QWidget* parent) : QWidget(parent) {
	myText = new QTextEdit(this);	
	myText->setReadOnly(true);
}
	
void View::setModel(const Model* model) {
	myModel = model;
}

//typedef std::vector<const Book*>::const_iterator BooksIt;

void View::drawModel() const {
	myText->clear();
	const std::vector<const Book*> books = myModel->getBooks();
	for (size_t i = 0; i < books.size(); ++i) {
		myText->append(books[i]->getTitle().c_str());
		myText->append("\n");
	}
}


