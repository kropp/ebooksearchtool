#include "handler.h"

static const QString BOOK_FORMAT = "pdf";

OPDSHandler::OPDSHandler(Data* data, SearchResult& result) : mySearchResult(result) {
    myData = data;
    myIsEntry = false;
    myIsInContent = false;    
    myFormat = BOOK_FORMAT;
}

/*OPDSHandler::OPDSHandler(Data* data, const XMLInputSource* source) {
    myData = data;
    myIsEntry = false;
    myIsInContent = false;    
    myFormat = BOOK_FORMAT;
    myInputFile = source;
    qDebug() << "OPDSHandler::OPDSHandler source data" << myInputFile->getData(10, 20);
}*/
    
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
        myBook = new Book();
        setInitialValues();
    } else if (tag == TAG_LINK) {
        processLink(attributes);
    } else if (tag == TAG_CONTENT) {
        myIsInContent = true;
    } else if (tag == TAG_CATEGORY) {
        myBook->addCategory(attributes.value(ATTRIBUTE_TERM));
    }
	return true;
}

bool OPDSHandler::endElement (const QString& namespaceUri, const QString& tag, const QString& ) {
	//qDebug() << "Handler::endElement namespace " << namespaceUri;
	if (!myIsEntry) {
        if ((tag == "totalResults") && 
            (namespaceUri == NSPASE_OPENSEARCH)) {
        //qDebug() << "totalResults namespace" << myCurrentText << namespaceUri;
            myData->setTotalEntries(myCurrentText.toInt());
        }
        return true;
    }
   
   //if I am inside entry
    if (namespaceUri == NSPACE_ATOM) {
        if (tag == TAG_ENTRY) {
            myData->addBook(myBook);
	        myIsEntry = false;	
    	} else if (tag == TAG_TITILE) {
	    	myBook->setTitle(myCurrentText);
	    } else if (tag == TAG_NAME) {
		    myAuthorsName = myCurrentText;
	    } else if (tag == TAG_URI) {
		    myAuthorsUri = myCurrentText;
	    } else if (tag == TAG_ID) {
	        myBook->setId(myCurrentText);
        } else if (tag == TAG_AUTHOR) {
            const Author* author = new Author(myAuthorsName, myAuthorsUri); 
	        myBook->addAuthor(author);
        } else if (tag == TAG_SUMMARY) {
	        myBook->setSummary(myCurrentText);
	    } else if (tag == TAG_RIGHTS) {
            myBook->setRights(myCurrentText);
	    } else if (tag == TAG_UPDATED) {
            myBook->setUpdated(myCurrentText);
        } else if (tag == TAG_CONTENT) {
            myBook->setContent(myCurrentText);
            myIsInContent = false;
        } else if (myIsInContent) {
            myCurrentText += "<" + tag + "/>";
        }
    } else if (namespaceUri == NSPASE_DCTERMS) {
	    if (tag == TAG_LANGUAGE) { 
            myBook->setLanguage(myCurrentText);
        } else if (tag == TAG_ISSUED) {
            myBook->setIssued(myCurrentText);
        } else if (tag == TAG_PUBLISHER) {
            myBook->setPublisher(myCurrentText);
        }
     }
	return true;
}

void OPDSHandler::processLink(const QXmlAttributes& attributes) {
    // parse information about feed
    if (!myIsEntry) {
	    if (attributes.value(ATTRIBUTE_TYPE) == "application/atom+xml") { 
		   if (attributes.value(ATTRIBUTE_RELATION) == "next")  {
                //qDebug() << "Handler:: link to the next page " << attributes.value("href");
                mySearchResult.setLinkToNextResult(myOpdsCatalog, attributes.value("href"));
            } else if (attributes.value(ATTRIBUTE_RELATION) == "self") {
                //qDebug() << "Handler:: link to self " << attributes.value("href");
                myOpdsCatalog = attributes.value("href");
            }
       }

        return;
    }		
// if I am inside entry 
    if (attributes.value(ATTRIBUTE_RELATION) == ATTR_VALUE_ACQUISITION) {
        QString format = attributes.value(ATTRIBUTE_TYPE);
        format.remove("application/");
        myBook->addSourceLink(format, attributes.value(ATTRIBUTE_REFERENCE));
                 
    } else if ((attributes.value(ATTRIBUTE_TYPE).contains("image")) && 
               ((attributes.value(ATTRIBUTE_RELATION) == ATTR_VALUE_COVER) ||
                (attributes.value(ATTRIBUTE_RELATION) == ATTR_VALUE_COVER_STANZA) || 
                (attributes.value(ATTRIBUTE_RELATION) == ATTR_VALUE_COVER_))) {
        
                myBook->setCoverLink(attributes.value(ATTRIBUTE_REFERENCE));
    }
}

void OPDSHandler::setInitialValues() {
    myAuthorsName = "";
    myAuthorsUri = "";
}
