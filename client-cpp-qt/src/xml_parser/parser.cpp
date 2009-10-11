#include <QtXml>

#include "parser.h"

bool AtomHandler::characters (const QString& strText) {
	myStrText = strText;
	return true;
}

bool AtomHandler::startElement (const QString& , const QString& , const QString& name, const QXmlAttributes& ) {
	if (name == "entry") {
		myIsEntry = true;
	}
	return true;
}

bool AtomHandler::endElement (const QString&, const QString&, const QString& str) {
	if (str == "entry") {
		const Author* author = new Author(myAuthorsName.toStdString(), myAuthorsUri.toStdString()); 
		Book* book = new Book(myTitle.toStdString(), myLanguage.toStdString(), mySummary.toStdString(), myBooksUri.toStdString());
		book->addAuthor(author);
		myModel->addBook(book);
		myIsEntry = false;	
	}	
	if (!myIsEntry) {
		return true;
	}
	if (str == "title") {
		myTitle = myStrText;
	} else if (str == "name") {
		myAuthorsName = myStrText;
	} else if (str == "uri") {
		myAuthorsUri = myStrText;
	} else if (str == "id") {
		myBooksUri = myStrText;
	} else if (str == "dc:language") {
		myLanguage = myStrText;
	} else if (str == "dc:description") {
		mySummary = myStrText;
	}
	return true;
}

AtomParser::AtomParser() {}

void AtomParser::parse(QFile* file, Model* model) {
	AtomHandler handler(model);
	QXmlInputSource source(file);
	QXmlSimpleReader reader;
	reader.setContentHandler(&handler);
	reader.parse(source);
}



