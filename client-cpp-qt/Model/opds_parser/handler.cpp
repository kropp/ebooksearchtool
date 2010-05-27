#include "handler.h"
#include "../catalog.h"

OPDSHandler::OPDSHandler(QVector<Book*>* data, SearchResult& result) : mySearchResult(&result) {
    myBookData = data;
    myCatalogData = 0;
    myIsEntry = false;
    myIsInContent = false;
    catalogLinkUrl = 0;
    currentParsedServer = "";
    myParseLinksMode = false;
    myLinksInformation = 0;

}

OPDSHandler::OPDSHandler(QVector<Book*>* bookData, QVector<Catalog*>* catalogData, QString parsedServer, SearchResult& result)
    : mySearchResult(&result)
{
    myBookData = bookData;
    myCatalogData = catalogData;
    myIsEntry = false;
    myIsInContent = false;
    currentParsedServer = parsedServer;
    myParseLinksMode = false;
    myLinksInformation = 0;
}

OPDSHandler::OPDSHandler(LinksInformation* linksInfo) : mySearchResult(0) {
    myLinksInformation = linksInfo;
    myParseLinksMode = true;
    myIsEntry = false;
    myIsInContent = false;
    currentParsedServer = "";
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
    mySourceLinks = new QMap<QString, QString>();
    myLocalLink.first = QString();
    myLocalLink.second = QString();

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
        Catalog* newCatalog = new Catalog(true, title, content, catalogLinkUrl);
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
        newBook->setSourceLinks(mySourceLinks);
        if (!myLocalLink.first.isEmpty()) {
            newBook->setLocalLink(myLocalLink);
            qDebug() << "OPDSHandler::endEntry() local link parsed "<< myLocalLink.second;
        }
        myBookData->append(newBook);
    }
}

bool OPDSHandler::startElement (const QString& namespaceUri, const QString& tag, const QString&, const QXmlAttributes& attributes)
{
    if (!myIsEntry && myParseLinksMode && tag == TAG_LINK) {
        parseCatalogLinks(attributes);
        return true;
    }

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

void OPDSHandler::parseCatalogLinks(const QXmlAttributes& attributes) {

    if (attributes.value(ATTRIBUTE_RELATION) == ATTR_VALUE_RELATION_NEW) {
        qDebug() << "OPDSHandler::startElement parse links mode" << attributes.value(ATTRIBUTE_REFERENCE);
        myLinksInformation->addNewLink(attributes.value(ATTRIBUTE_REFERENCE));
    } else if (attributes.value(ATTRIBUTE_RELATION) == ATTR_VALUE_RELATION_POPULAR) {
        qDebug() << "OPDSHandler::startElement parse links mode" << attributes.value(ATTRIBUTE_REFERENCE);
        myLinksInformation->addPopularLink(attributes.value(ATTRIBUTE_REFERENCE));
    }
}

bool OPDSHandler::endElement (const QString& namespaceUri, const QString& tag, const QString& )
{
    if (myParseLinksMode) {
        return true;
    }

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
               // qDebug() << "Handler:: link to the next page " << attributes.value("href");
                mySearchResult->setLinkToNextResult(myOpdsCatalog, attributes.value("href"));
            } else if (attributes.value(ATTRIBUTE_RELATION) == "self") {
               // qDebug() << "Handler:: link to self " << attributes.value("href");
                myOpdsCatalog = attributes.value("href");
            }
        }

        return;
    }

    // if I am inside an entry
    if (attributes.value(ATTRIBUTE_RELATION).contains(ATTR_VALUE_ACQUISITION)) {

        QString format = attributes.value(ATTRIBUTE_TYPE);
        format.remove("application/");
        if (attributes.value(ATTRIBUTE_RELATION) == ATTR_VALUE_ACQUISITION_LOCAL) {
            myLocalLink.first = format;
            myLocalLink.second = attributes.value(ATTRIBUTE_REFERENCE);
        } else {
            mySourceLinks->insert(format, attributes.value(ATTRIBUTE_REFERENCE));
        }

    } else if ((attributes.value(ATTRIBUTE_TYPE).contains("image")) && 
               ((attributes.value(ATTRIBUTE_RELATION) == ATTR_VALUE_COVER) ||
                (attributes.value(ATTRIBUTE_RELATION) == ATTR_VALUE_COVER_STANZA) || 
                (attributes.value(ATTRIBUTE_RELATION) == ATTR_VALUE_COVER_))) {
        
        coverLink = (attributes.value(ATTRIBUTE_REFERENCE));
    } else if (attributes.value(ATTRIBUTE_TYPE) == "application/atom+xml") {
        catalogLinkUrl = new UrlData(attributes.value("href") ,currentParsedServer);
    }
}
