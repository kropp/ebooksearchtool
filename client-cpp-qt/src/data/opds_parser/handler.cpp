#include "handler.h"

static const QString BOOK_FORMAT = "pdf";

OPDSHandler::OPDSHandler(Data* data) {
    myData = data;
    myIsEntry = false;
    myIsInContent = false;    
    myFormat = BOOK_FORMAT;
}

OPDSHandler::~OPDSHandler() {}


bool OPDSHandler::characters (const QString& strText) {
	myCurrentText += strText;
	return true;
}

bool OPDSHandler::startElement (const QString& namespaceUri, const QString& tag, const QString&, const QXmlAttributes& attributes) {
    if (myIsInContent) {
        myCurrentText += "<" + tag + ">";
        return true;
    }
    myCurrentText = "";
    if (namespaceUri != NSPACE_ATOM) {
        return true;
    }
    if (tag == TAG_ENTRY) {
        myIsEntry = true;
        setInitialValues();
    } else if (tag == TAG_LINK) {
        processLink(attributes);
    } else if (tag == TAG_CONTENT) {
        qDebug() << "OPDSHandler start content parsing";
        myIsInContent = true;
    }
	return true;
}

bool OPDSHandler::endElement (const QString& namespaceUri, const QString& localName, const QString& tag) {
	//qDebug() << "Handler::endElement namespace " << namespaceUri;
	if ((!myIsEntry) && (localName == "totalResults") && (namespaceUri == NSPASE_OPENSEARCH)) {
        //qDebug() << "totalResults namespace" << myCurrentText << namespaceUri;
        myData->setTotalEntries(myCurrentText.toInt());
        return true;
    }
    
// if (myIsEntry)  
    if (tag == TAG_ENTRY) {
		    const Author* author = new Author(myAuthorsName, myAuthorsUri); 
		    Book* book = new Book(myTitle,
		                          myLanguage, 
		                          mySummary, 
		                          myBooksUri);
		    book->addAuthor(author);
		    book->setSourceLink(myFormat, myBooksLink);
            book->setCoverLink(myBooksCover);
		    book->setContent(myContent);
            myData->addBook(book);
		    myIsEntry = false;	
	} else if (tag == TAG_TITILE) {
		myTitle = myCurrentText;
	} else if (tag == TAG_NAME) {
		myAuthorsName = myCurrentText;
	} else if (tag == TAG_URI) {
		myAuthorsUri = myCurrentText;
	} else if (tag == TAG_ID) {
		myBooksUri = myCurrentText;
	} else if ((localName == "language") && (namespaceUri == NSPASE_DCTERMS)) {
		//qDebug() << "Handler::endElement namespace for language " << namespaceUri;
      //qDebug() << "language" << myCurrentText;
      myLanguage = myCurrentText;
	} else if (tag == TAG_SUMMARY) {
		mySummary = myCurrentText;
	} else if (tag == TAG_CONTENT) {
        // set content
        qDebug() << "OPDSHandler parser content finished";
        qDebug() << "content " << myCurrentText;
        myContent = myCurrentText;
        myIsInContent = false;
    } else if (myIsInContent) {
        myCurrentText += "<" + tag + "/>";
    }
	return true;
}

void OPDSHandler::processLink(const QXmlAttributes& attributes) {
    if (!myIsEntry) {
	    if ((attributes.value(ATTRIBUTE_TYPE) == "application/atom+xml") && 
		   (attributes.value(ATTRIBUTE_RELATION) == "next") && 
		   (attributes.value(ATTRIBUTE_TITLE) == "Next Page"))  {
		       // myData->setLinkToNextPage(attributes.value("href"));
            //qDebug() << "Handler:: link to the next page " << attributes.value("href");
            }
        return;
    }		
// if I am inside entry 
    if ((attributes.value(ATTRIBUTE_TYPE) == "application/" + myFormat) &&
       (attributes.value(ATTRIBUTE_RELATION) == ATTR_VALUE_ACQUISITION)) {
        
                myBooksLink = attributes.value(ATTRIBUTE_REFERENCE);
	
    } else if ((attributes.value(ATTRIBUTE_TYPE).contains("image")) && 
               ((attributes.value(ATTRIBUTE_RELATION) == ATTR_VALUE_COVER) ||
                (attributes.value(ATTRIBUTE_RELATION) == ATTR_VALUE_COVER_STANZA))) {
        
                myBooksCover = attributes.value(ATTRIBUTE_REFERENCE);
    }
}

void OPDSHandler::setInitialValues() {
    myTitle = "";
    myLanguage = "";
    mySummary = "";
    myAuthorsName = "";
    myAuthorsUri = "";
    myBooksUri = "";
    myBooksLink = "";
    myBooksCover = "";
    myContent = "";
}
