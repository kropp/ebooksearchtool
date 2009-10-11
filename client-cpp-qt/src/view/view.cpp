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

void View::drawModel() const {
	myTextBrowser->clear();
	const std::vector<const Book*> books = myModel->getBooks();
	for (size_t i = 0; i < books.size(); ++i) {
		myTextBrowser->insertHtml(bookToHtml(books[i]));
	}
	
}

QString View::bookToHtml(const Book* book) const {
	QString html;
//	appendHeader(html, book->getTitle().c_str());
	appendReference(html, book->getUri().c_str(), book->getTitle().c_str());
	appendParagraph(html, book->getAuthor()->getName().c_str());
	appendParagraph(html, "Summary: ");	
	appendParagraph(html, book->getSummary().c_str());
	appendParagraph(html, "author's uri: ");
	appendParagraph(html, book->getAuthor()->getUri().c_str());
	appendParagraph(html, " ");
	return html;
}

void View::appendParagraph(QString& html, const QString& paragraph) const {
	html.append("<p>");
	html.append(paragraph);
	html.append("</p>");
}

void View::appendHeader(QString& html, const QString& header) const {
	html.append("<h3>");
	html.append(header);
	html.append("</h3>");	
}

void View::appendReference(QString& html, const QString& reference, const QString& text) const {
	html.append("<a href=\""); //<a href="subfolder/page2.htm">Щёлкните здесь для перехода на page 2</a>
	html.append(reference);
	html.append("\">");
	html.append(text);
	html.append("</a>");
}
