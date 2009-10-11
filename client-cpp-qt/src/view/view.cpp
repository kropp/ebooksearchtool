#include "view.h"

#include <QTextEdit>
#include <QFlags>

#include <iostream>

View::View(QWidget* parent) : QWidget(parent), myOneBookMode(false), myReadingProcess(0) {
	myTextBrowser = new QTextBrowser(this);	
	myTextBrowser->setReadOnly(true);
	myTextBrowser->resize(1000, 700); //сделать автоматическое удобное задание размеров
	connect(myTextBrowser, SIGNAL(sourceChanged(const QUrl&)), this, SLOT(downloadFile(const QUrl&)));
}
	
void View::setModel(const Model* model) {
	myModel = model;
	drawModel();
}

void View::drawModel() {
	myOneBookMode = (myModel->getSize() > 1) ? false : true;
	myTextBrowser->clear();
	const std::vector<const Book*> books = myModel->getBooks();
	for (size_t i = 0; i < books.size(); ++i) {
		myTextBrowser->insertHtml(bookToHtml(books[i]));
	}
	
}

void View::downloadFile(const QUrl& url) {
	QString str = url.toString();	
	std::cout << "view url-refirence str" << str.toStdString().c_str()<< " \n";
	if (!str.contains(QString("READ"))) {
		str.append(".atom");
		myOneBookMode = true;
		std::cerr << "reference not READ\n";
		emit urlRequest(str);
	} else {
		std::cerr << "reference is READ\n";
		myOneBookMode = false;	
		if (!myReadingProcess) {
			myReadingProcess = new QProcess(this);
		}
		myReadingProcess->start (QString("FBReader"), QStringList("Proust - Within A Budding Grove.epub"));
	}
}


QString View::bookToHtml(const Book* book) const {
	QString html;
	if (myOneBookMode) {
		appendHeader(html, book->getTitle().c_str());
		//appendReference();
	} else {
		appendReference(html, book->getUri().c_str(), book->getTitle().c_str());
	}
	appendParagraph(html, book->getAuthor()->getName().c_str());
	appendParagraph(html, "Summary: ");	
	appendParagraph(html, book->getSummary().c_str());
	//appendParagraph(html, "author's uri: ");
	//appendParagraph(html, book->getAuthor()->getUri().c_str());
	appendParagraph(html, " ");
	if (myOneBookMode) {
		appendReference(html, "READ", "READ");
	}	
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
