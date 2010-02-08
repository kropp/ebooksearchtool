#include "handler.h"

const QString AtomHandler::ourOpensearchUri = "http://a9.com/-/spec/opensearch/1.1/";
const QString AtomHandler::ourDctermsUri = "http://purl.org/dc/terms/";

const QString AtomHandler::ourConfigFilePath = "../.config.ini";

AtomHandler::AtomHandler(Data* data) {
    myData = data;
    myIsEntry = false;
    
    QSettings settings(ourConfigFilePath, QSettings::IniFormat);
    myFormat = settings.value("parser/format").toString();
}

AtomHandler::~AtomHandler() {}


bool AtomHandler::characters (const QString& strText) {
	myCurrentText += strText;
	return true;
}

bool AtomHandler::startElement (const QString& , const QString& , const QString& name, const QXmlAttributes& attributes) {
    myCurrentText = "";
    if (!myIsEntry) {
	    if ((name == "link") && 
	       (attributes.value("type") == "application/atom+xml") && 
		   (attributes.value("rel") == "next") && 
		   (attributes.value("title") == "Next Page"))  {
		       // myData->setLinkToNextPage(attributes.value("href"));
            //qDebug() << "Handler:: link to the next page " << attributes.value("href");
        } else if (name == "entry") {
		    myIsEntry = true;
	    }     
	    return true;    
    }	
// if myIsEntry
	if ((name == "link") &&
        (attributes.value("type") == "application/" + myFormat) &&
        (attributes.value("rel") == "http://opds-spec.org/acquisition")) {
        
        myBooksLink = attributes.value("href");
	}

	if ((name == "link") && 
        (attributes.value("type") == "image/png") && 
        (attributes.value("rel") == "http://opds-spec.org/cover")) {
        
        myBooksCover = attributes.value("href");
	}

	return true;
}

bool AtomHandler::endElement (const QString& namespaceUri, const QString& localName, const QString& str) {
	//qDebug() << "Handler::endElement namespace " << namespaceUri;
	if ((!myIsEntry) && (localName == "totalResults") && (namespaceUri == ourOpensearchUri)) {
        qDebug() << "totalResults namespace" << myCurrentText << namespaceUri;
        myData->setTotalEntries(myCurrentText.toInt());
        return true;
    }
    
// if (myIsEntry)  
    if (str == "entry") {
		    const Author* author = new Author(myAuthorsName, myAuthorsUri); 
		    Book* book = new Book(myTitle,
		                          myLanguage, 
		                          mySummary, 
		                          myBooksUri);
		    book->addAuthor(author);
		    book->setSourceLink(myBooksLink, myFormat);
            book->setCoverLink(myBooksCover);
		    myData->addBook(book);
		    myIsEntry = false;	
	} else if (str == "title") {
		myTitle = myCurrentText;
	} else if (str == "name") {
		myAuthorsName = myCurrentText;
	} else if (str == "uri") {
		myAuthorsUri = myCurrentText;
	} else if (str == "id") {
		myBooksUri = myCurrentText;
	} else if ((localName == "language") && (namespaceUri == ourDctermsUri)) {
		qDebug() << "Handler::endElement namespace for language " << namespaceUri;
      qDebug() << "language" << myCurrentText;
      myLanguage = myCurrentText;
	} else if (str == "summary") {
		mySummary = myCurrentText;
	}
	return true;
}
