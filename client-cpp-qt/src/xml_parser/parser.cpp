#include <QtXml>

#include "parser.h"

#include <iostream>

bool AtomHandler::characters (const QString& strText) {
	myStrText = strText;
	return true;
}

bool AtomHandler::startElement (const QString& , const QString& , const QString& name, const QXmlAttributes& ) {
	//std::cerr << "begin to handle start element\n";
	if (name == "entry") {
		std::cerr << "start tag entry was found\n";
		myIsEntry = true;
	}
	return true;
}

bool AtomHandler::endElement (const QString&, const QString&, const QString& str) {
//std::cerr << "start to handle end-element\n";
	if (str == "entry") {
		const Author* author = new Author(myAuthorsName.toStdString());
		Book* book = new Book(myTitle.toStdString(), myLanguage.toStdString(), mySummary.toStdString());
		book->addAuthor(author);
		myModel->addBook(book);
		std::cerr << "A book has been added to the model\n";
		myIsEntry = false;	
	}	
	if (!myIsEntry) {
		return true;
	}
	if (str == "title") {
		myTitle = myStrText;
		std::cerr << "title\n";
	} else if (str == "name") {
		myAuthorsName = myStrText;
		std::cerr << "author's name\n";
	} else if (str == "dc:language") {
		myLanguage = myStrText;
		std::cerr << "language\n";
	} else if (str == "dc:description") {
		mySummary = myStrText;
		std::cerr << "summary\n";
	}
	return true;
}

AtomParser::AtomParser() {}

void AtomParser::parse(QFile* file, Model* model) {
	std::cerr << "parser started\n";
	AtomHandler handler(model);
	QXmlInputSource source(file);
	QXmlSimpleReader reader;
	reader.setContentHandler(&handler);
	reader.parse(source);
}



