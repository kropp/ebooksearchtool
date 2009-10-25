#include <QtXml>

#include "parser.h"

AtomHandler::AtomHandler(Model* model) {
    myModel = model;
    myIsEntry = false;
    myNextAtomPage = 0;
}


bool AtomHandler::characters (const QString& strText) {
	myStrText = strText;
	return true;
}

bool AtomHandler::startElement (const QString& , const QString& , const QString& name, const QXmlAttributes& attributes) {
    if (!myIsEntry) {
	    if ((name == "link") && 
	       (attributes.value("type") == "application/atom+xml") && 
		   (attributes.value("rel") == "next") && 
		   (attributes.value("title") == "Next Page"))  {
		    if (myNextAtomPage == 0) {
		        myNextAtomPage = new QString();
		    }
			*myNextAtomPage = attributes.value("href");
         //   std::cout << "link to the next page " << myNextAtomPage->toStdString().c_str() << "\n";
        } else if (name == "entry") {
		    myIsEntry = true;
	    }     
	    return true;    
    }	
// if myIsEntry
	if ((name == "link") && (attributes.value("type") == "application/epub+zip")) {
        myBooksLink = attributes.value("href");
	}
	//<link type="application/atom+xml" rel="next" title="Next Page" href="http://feedbooks.com/books/search.atom?page=2&amp;query=Doyle"/>
	return true;
}

bool AtomHandler::endElement (const QString&, const QString&, const QString& str) {
	if ((!myIsEntry) && (str == "opensearch:totalResults")) {
        myModel->setTotalEntries(myStrText.toInt());
        return true;
    }
    
// if (myIsEntry)  
    if (str == "entry") {
		    const Author* author = new Author(myAuthorsName.toStdString(), myAuthorsUri.toStdString()); 
		    Book* book = new Book(myTitle.toStdString(), 
		                          myLanguage.toStdString(), 
		                          mySummary.toStdString(), 
		                          myBooksUri.toStdString());
		    book->addAuthor(author);
		    book->setLink(myBooksLink.toStdString());
		    myModel->addBook(book);
		    myIsEntry = false;	
	} else if (str == "title") {
		myTitle = myStrText;
	} else if (str == "name") {
		myAuthorsName = myStrText;
	} else if (str == "uri") {
		myAuthorsUri = myStrText;
	} else if (str == "id") {
		myBooksUri = myStrText;
	} else if (str == "dc:language") {
		myLanguage = myStrText;
	} else if (str == "summary") {
		mySummary = myStrText;
	}
	return true;
}

AtomParser::AtomParser() : myNextAtomPage(0) {}

void AtomParser::parse(QFile* file, Model* model) {
	AtomHandler handler(model);
	QXmlInputSource source(file);
	QXmlSimpleReader reader;
	reader.setContentHandler(&handler);
	reader.parse(source);
	myNextAtomPage = handler.myNextAtomPage;
}

const QString* AtomParser::getNextAtomPage() const {
    return myNextAtomPage;
}

