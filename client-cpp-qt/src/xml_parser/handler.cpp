#include "handler.h"

QString AtomHandler::ourConfigFilePath = "../.config.ini";

AtomHandler::AtomHandler(Data* data) {
    myData = data;
    myIsEntry = false;
    myNextAtomPage = 0;
    
    QSettings settings(ourConfigFilePath, QSettings::IniFormat);
    myFormat = settings.value("parser/format").toString();
}


bool AtomHandler::characters (const QString& strText) {
	myCurrentText += strText;
	return true;
}

bool AtomHandler::startElement (const QString& , const QString& , const QString& name, const QXmlAttributes& attributes) {
    myCurrentText = "";
    if (!myIsEntry) {
	    if ((name == "link") && 
	       (attributes.value("type") == "application/"+myFormat) && 
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
	if ((name == "link") && (attributes.value("type") == "application/pdf")) {
        myBooksLink = attributes.value("href");
	}
	if ((name == "link") && (attributes.value("type") == "image/png") && (attributes.value("rel") == "http://opds-spec.org/thumbnail")) {
        myBooksCover = attributes.value("href");
	}

	return true;
}

bool AtomHandler::endElement (const QString&, const QString&, const QString& str) {
	if ((!myIsEntry) && (str == "opensearch:totalResults")) {
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
		    book->setSourceLink(myBooksLink);
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
	} else if (str == "dc:language") {
		myLanguage = myCurrentText;
	} else if (str == "summary") {
		mySummary = myCurrentText;
	}
	return true;
}
