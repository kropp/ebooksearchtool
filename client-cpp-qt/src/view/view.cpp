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
		myTextBrowser->insertHtml(bookToHtml(books[i]));
		//myTextBrowser->setText(bookToHtml(books[i]));
/*		myTextBrowser->append(books[i]->getTitle().c_str());
		myTextBrowser->append(books[i]->getAuthor()->getName().c_str());
		myTextBrowser->append("\nSummary: ");	
		myTextBrowser->append(books[i]->getSummary().c_str());
		myTextBrowser->append("author's uri: ");
		myTextBrowser->append(books[i]->getAuthor()->getUri().c_str());
		myTextBrowser->append("book's uri: ");
		myTextBrowser->append(books[i]->getUri().c_str());
		myTextBrowser->append("-------------------------------------------");*/
	}
	
}

QString View::bookToHtml(const Book* book) const {
	QString html;
	appendParagraph(html, book->getTitle().c_str());
	appendParagraph(html, book->getAuthor()->getName().c_str());
	appendParagraph(html, "\nSummary: ");	
	appendParagraph(html, book->getSummary().c_str());
	appendParagraph(html, "author's uri: ");
	appendParagraph(html, book->getAuthor()->getUri().c_str());
	appendParagraph(html, "book's uri: ");
	appendParagraph(html, book->getUri().c_str());
	appendParagraph(html, "-----------------------------------------");
	appendParagraph(html, " ");
	return html;
}

void View::appendParagraph(QString& html, const QString& paragraph) const {
	html.append("<p>");
	html.append(paragraph);
	html.append("</p>");
}


