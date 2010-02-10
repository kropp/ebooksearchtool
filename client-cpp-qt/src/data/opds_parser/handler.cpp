#include "handler.h"

static const QString NSPASE_OPENSEARCH = "http://a9.com/-/spec/opensearch/1.1/";
static const QString NSPASE_DCTERMS = "http://purl.org/dc/terms/";
static const QString NSPACE_ATOM = "http://www.w3.org/2005/Atom";

static const QString TAG_LINK = "link";
static const QString TAG_TITILE = "title";
static const QString TAG_ENTRY = "entry";
static const QString TAG_NAME = "name";
static const QString TAG_URI = "uri";
static const QString TAG_ID = "id";
static const QString TAG_CONTENT = "content";

static const QString ATTRIBUTE_TYPE = "type";
static const QString ATTRIBUTE_REFERENCE = "href";
static const QString ATTRIBUTE_RELATIONSHIP = "rel";
static const QString ATTRIBUTE_TITLE = "title";

static const QString ATTR_VALUE_ACQUISITION = "http://opds-spec.org/acquisition";
static const QString ATTR_VALUE_COVER = "http://opds-spec.org/cover";

static const QString BOOK_FORMAT = "pdf";

OPDSHandler::OPDSHandler(Data* data) {
    myData = data;
    myIsEntry = false;
    
    myFormat = BOOK_FORMAT;
}

OPDSHandler::~OPDSHandler() {}


bool OPDSHandler::characters (const QString& strText) {
	myCurrentText += strText;
	return true;
}

bool OPDSHandler::startElement (const QString& namespaceUri, const QString& tag, const QString&, const QXmlAttributes& attributes) {
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
	} else if (tag == "summary") {
		mySummary = myCurrentText;
	}
	return true;
}

void OPDSHandler::processLink(const QXmlAttributes& attributes) {
    if (!myIsEntry) {
	    if ((attributes.value(ATTRIBUTE_TYPE) == "application/atom+xml") && 
		   (attributes.value(ATTRIBUTE_RELATIONSHIP) == "next") && 
		   (attributes.value(ATTRIBUTE_TITLE) == "Next Page"))  {
		       // myData->setLinkToNextPage(attributes.value("href"));
            //qDebug() << "Handler:: link to the next page " << attributes.value("href");
            }
        return;
    }		
// if I am inside entry 
    if ((attributes.value(ATTRIBUTE_TYPE) == "application/" + myFormat) &&
       (attributes.value(ATTRIBUTE_RELATIONSHIP) == ATTR_VALUE_ACQUISITION)) {
        
                myBooksLink = attributes.value(ATTRIBUTE_REFERENCE);
	
    } else if ((attributes.value(ATTRIBUTE_TYPE).contains("image")) && 
              (attributes.value(ATTRIBUTE_RELATIONSHIP) == ATTR_VALUE_COVER)) {
        
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
}
