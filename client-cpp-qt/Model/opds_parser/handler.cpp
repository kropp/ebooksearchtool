#include "handler.h"
#include "../catalog.h"

static const QString BOOK_FORMAT = "pdf";

OPDSHandler::OPDSHandler(QVector<Book*>* data, SearchResult& result) : mySearchResult(result) {
    myBookData = data;
    myCatalogData = 0;
    myIsEntry = false;
    myIsInContent = false;
    catalogLinkUrl = 0;
    myFormat = BOOK_FORMAT;
    currentParsedServer = "";
}

OPDSHandler::OPDSHandler(QVector<Book*>* bookData, QVector<Catalog*>* catalogData, QString parsedServer, SearchResult& result)
    : mySearchResult(result)
{
    myBookData = bookData;
    myCatalogData = catalogData;
    myIsEntry = false;
    myIsInContent = false;
    myFormat = BOOK_FORMAT;
    currentParsedServer = parsedServer;
}

OPDSHandler::~OPDSHandler() {}

bool OPDSHandler::characters (const QString& strText) {
    myCurrentText += strText;
    return true;
}

void OPDSHandler::startNewEntry()
{
    categories = new QVector<QString>();
    authors = new QVector<Author*>();
    links = new QMap<QString, QString>();

    title = "";
    authorsName = "";
    authorsUri = "";
    entryId = "";
    entryAuthor = 0;
    summary = "";
    rights = "";
    updated = "";
    content = "";
    language = "";
    issued = "";
    publisher = "";
    coverLink = "";
    catalogLinkUrl = 0;
}

void OPDSHandler::endEntry()
{
    if (!entryAuthor && myCatalogData != 0)
    {
        Catalog* newCatalog = new Catalog(true, title, catalogLinkUrl);
        myCatalogData->append(newCatalog);
    }
    else
    {
        Book* newBook = new Book();

        newBook->setTitle(title);

        for (int i = 0; i < authors->size(); i++)
        {
            newBook->addAuthor(authors->at(i));
        }

        newBook->setId(entryId);
        newBook->setSummary(summary);
        newBook->setRights(rights);
        newBook->setUpdated(updated);
        newBook->setContent(content);
        newBook->setLanguage(language);
        newBook->setIssued(issued);
        newBook->setPublisher(publisher);
        newBook->setCoverLink(coverLink);

        myBookData->append(newBook);
    }
}

bool OPDSHandler::startElement (const QString& namespaceUri, const QString& tag, const QString&, const QXmlAttributes& attributes)
{

    if (myIsInContent)
    {
        myCurrentText += "<" + tag + ">";
        return true;
    }

    myCurrentText = "";

    if (namespaceUri != NSPACE_ATOM)
    {
        return true;
    }

    if (tag == TAG_ENTRY)
    {
        myIsEntry = true;
        startNewEntry();
        setInitialValues();
    }
    else if (tag == TAG_LINK)
    {
        processLink(attributes);
    }
    else if (tag == TAG_CONTENT)
    {
        myIsInContent = true;
    }
    else if (tag == TAG_CATEGORY)
    {
        categories->append(attributes.value(ATTRIBUTE_TERM));
    }

    return true;
}

bool OPDSHandler::endElement (const QString& namespaceUri, const QString& tag, const QString& )
{
    if (myIsEntry)
    {
        if (namespaceUri == NSPACE_ATOM)
        {
            if (tag == TAG_ENTRY)
            {
                endEntry();
	    	myIsEntry = false;	
            }
            else if (tag == TAG_TITILE)
            {
                title = myCurrentText;
            }
            else if (tag == TAG_NAME)
            {
                authorsName = myCurrentText;
            }
            else if (tag == TAG_URI)
            {
                authorsUri = myCurrentText;
            }
            else if (tag == TAG_ID)
            {
                entryId = myCurrentText;
            }
            else if (tag == TAG_AUTHOR)
            {
                entryAuthor = new Author(authorsName, authorsUri);
                authors->append(entryAuthor);
            }
            else if (tag == TAG_SUMMARY)
            {
                summary = myCurrentText;
            }
            else if (tag == TAG_RIGHTS)
            {
                rights = myCurrentText;
            }
            else if (tag == TAG_UPDATED)
            {
                updated = myCurrentText;
            }
            else if (tag == TAG_CONTENT)
            {
                content = myCurrentText;
                myIsInContent = false;
            }
            else if (myIsInContent)
            {
                myCurrentText += "<" + tag + "/>";
            }
        } else if (namespaceUri == NSPASE_DCTERMS) {
            if (tag == TAG_LANGUAGE) {
                language = myCurrentText;
            } else if (tag == TAG_ISSUED) {
                issued = myCurrentText;
            } else if (tag == TAG_PUBLISHER) {
                publisher = myCurrentText;
            }
        }
    }

    return true;
}

void OPDSHandler::processLink(const QXmlAttributes& attributes) {
    // parse information about feed
    if (!myIsEntry) {
        if (attributes.value(ATTRIBUTE_TYPE) == "application/atom+xml") {
            if (attributes.value(ATTRIBUTE_RELATION) == "next")  {
                qDebug() << "Handler:: link to the next page " << attributes.value("href");
                mySearchResult.setLinkToNextResult(myOpdsCatalog, attributes.value("href"));
            } else if (attributes.value(ATTRIBUTE_RELATION) == "self") {
                qDebug() << "Handler:: link to self " << attributes.value("href");
                myOpdsCatalog = attributes.value("href");
            }
        }

        return;
    }

    // if I am inside an entry
    if (attributes.value(ATTRIBUTE_RELATION) == ATTR_VALUE_ACQUISITION) {
        QString format = attributes.value(ATTRIBUTE_TYPE);
        format.remove("application/");
        links->insert(format, attributes.value(ATTRIBUTE_REFERENCE));

    } else if ((attributes.value(ATTRIBUTE_TYPE).contains("image")) && 
               ((attributes.value(ATTRIBUTE_RELATION) == ATTR_VALUE_COVER) ||
                (attributes.value(ATTRIBUTE_RELATION) == ATTR_VALUE_COVER_STANZA) || 
                (attributes.value(ATTRIBUTE_RELATION) == ATTR_VALUE_COVER_))) {
        
        coverLink = (attributes.value(ATTRIBUTE_REFERENCE));
    } else if (attributes.value(ATTRIBUTE_TYPE) == "application/atom+xml") {
        catalogLinkUrl = new UrlData(attributes.value("href") ,currentParsedServer);
    }
}

void OPDSHandler::setInitialValues() {
    myAuthorsName = "";
    myAuthorsUri = "";
}
