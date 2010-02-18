#include <QFile>
#include <QtXml>

#include "opds_writer.h"
#include "../data.h"

static const QString IMAGE_PNG = "image/png";
static const QString IMAGE_JPEG = "image/jpeg";
static const QString IMAGE_GIF = "image/gif";

DataWriter::DataWriter() {}

void DataWriter::write(QFile* file, const Data& data) {
	QDomDocument* doc = new QDomDocument();
    dataToDomDocument(data, *doc);
	QTextStream out(file);
	doc->save(out, 4);
    delete doc;
}

void DataWriter::dataToDomDocument(const Data& data, QDomDocument& doc) {
    QDomProcessingInstruction instruction = doc.createProcessingInstruction("xml", "version=\"1.0\" encoding=\"UTF-8\"");
    doc.appendChild(instruction);
    QDomElement feed = doc.createElement("feed");
    feed.setAttribute("xmlns", NSPACE_ATOM);
    feed.setAttribute("xmlns:dcterms", NSPASE_DCTERMS);
    doc.appendChild(feed);
    const QList<const Book*>& books = data.getBooks();
    foreach (const Book* book, books) {
        QDomElement entry = doc.createElement(TAG_ENTRY);
	    bookToDomElement(*book, doc, entry);
        feed.appendChild(entry);
    }
}

void DataWriter::bookToDomElement(const Book& book, QDomDocument& doc, QDomElement& entry) {
    // append title
    appendTagAndText(doc, entry, TAG_TITILE, book.getTitle());
    
    //append id
    appendTagAndText(doc, entry, TAG_ID, book.getId());
    
    // append date of last updating
    if (!book.getUpdated().isEmpty())
        appendTagAndText(doc, entry, TAG_UPDATED, book.getUpdated());
   
    // append links 
    appendSourceLinks(doc, entry, book);
    appendCoverLink(doc, entry, book);

   //append issued   
    if(!book.getIssued().isEmpty())
        appendTagAndText(doc, entry, "dcterms:issued", book.getIssued());
   
     //append authors
    const QVector<const Author*>& authors = book.getAuthors();
    foreach (const Author* author, authors) {
        QDomElement authorElement = doc.createElement("author");
        entry.appendChild(authorElement);
        appendTagAndText(doc, authorElement, TAG_NAME, author->getName());
        appendTagAndText(doc, authorElement, TAG_URI, author->getUri());
    }
        
  
   // append categories
    const QVector<QString>& categories = book.getCategories();
    foreach (QString category, categories) {
        QDomElement element = doc.createElement(TAG_CATEGORY);
	    element.setAttribute(ATTRIBUTE_TERM, category);
	    entry.appendChild(element);	 
    }
   
   //append language   
    if(!book.getLanguage().isEmpty())
        appendTagAndText(doc, entry, "dcterms:language", book.getLanguage());
    
// append content
    appendTagAndText(doc, entry, TAG_CONTENT, book.getContent());
    
   // append summary
    if (!book.getSummary().isEmpty())
        appendTagAndText(doc, entry, TAG_SUMMARY, book.getSummary());
   
   //append rights
    if (!book.getRights().isEmpty())
        appendTagAndText(doc, entry, TAG_RIGHTS, book.getRights());
   
}

void DataWriter::appendCoverLink(QDomDocument& doc, QDomElement& entry, const Book& book) {
    if (!book.getCoverLink().isEmpty()) {
        QDomElement coverLink = doc.createElement(TAG_LINK);
	    const QString& coverLinkValue = book.getCoverLink();
        QString attributeTypeValue = IMAGE_PNG;
        if (coverLinkValue.contains(".jpeg")) {
            attributeTypeValue = IMAGE_JPEG ;
        } else if (coverLinkValue.contains(".gif")){
            attributeTypeValue = IMAGE_GIF;
        }
        coverLink.setAttribute(ATTRIBUTE_TYPE, attributeTypeValue);
	    coverLink.setAttribute(ATTRIBUTE_RELATION, ATTR_VALUE_COVER);
        coverLink.setAttribute(ATTRIBUTE_REFERENCE, coverLinkValue);
	    entry.appendChild(coverLink);	
    }
}

void DataWriter::appendSourceLinks(QDomDocument& doc, QDomElement& entry, const Book& book) {
    // append Source link
    const QMap<QString, QString>& links = book.getSourceLinks();
    typedef QMap<QString, QString>::const_iterator MapIt;
    for (MapIt it = links.constBegin(); it != links.constEnd(); ++it) {
        QDomElement textLink = doc.createElement(TAG_LINK);
	    textLink.setAttribute(ATTRIBUTE_TYPE, "application/" + it.key());
	    textLink.setAttribute(ATTRIBUTE_RELATION, ATTR_VALUE_ACQUISITION);
	    textLink.setAttribute(ATTRIBUTE_REFERENCE, it.value());
	    entry.appendChild(textLink);		
	}
}

void DataWriter::appendTagAndText(QDomDocument& doc, QDomElement& parentElement, const QString& tag, const QString& text) {
    if (text.isEmpty()) {
        return;
    }
    QDomElement newElement = doc.createElement(tag);
    parentElement.appendChild(newElement);
    QDomText domText = doc.createTextNode(text);
    newElement.appendChild(domText);
}
