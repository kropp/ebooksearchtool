#include "view.h"

#include <QTextEdit>

View::View(QWidget* parent) : QWidget(parent) {
	myTextBrowser = new QTextBrowser(this);	
	myTextBrowser->setReadOnly(true);
	myTextBrowser->setText("test for displaying something\n");
	myTextBrowser->resize(1000, 700); //сделать автоматическое удобное задание размеров
}
	
void View::setModel(const Model* model) {
	myModel = model;
	drawModel();
}

//typedef std::vector<const Book*>::const_iterator BooksIt;

void View::drawModel() const {
	myTextBrowser->clear();
	const std::vector<const Book*> books = myModel->getBooks();
	for (size_t i = 0; i < books.size(); ++i) {
		myTextBrowser->append(books[i]->getTitle().c_str());
		myTextBrowser->append(books[i]->getAuthor()->getName().c_str());
		myTextBrowser->append("\nSummary: ");	
		myTextBrowser->append(books[i]->getSummary().c_str());
		myTextBrowser->append("author's uri: ");
		myTextBrowser->append(books[i]->getAuthor()->getUri().c_str());
		myTextBrowser->append("book's uri: ");
		myTextBrowser->append(books[i]->getUri().c_str());
		myTextBrowser->append("-------------------------------------------");
	}
	
}


