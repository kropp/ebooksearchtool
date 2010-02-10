#include <QFile>
#include <QtXml>

#include "opds_writer.h"
#include "../data.h"

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

    //append authors
    const QVector<const Author*>& authors = book.getAuthors();
    foreach (const Author* author, authors) {
        QDomElement authorElement = doc.createElement("author");
        entry.appendChild(authorElement);
        appendTagAndText(doc, authorElement, TAG_NAME, author->getName());
        appendTagAndText(doc, authorElement, TAG_URI, author->getUri());
    }
   
   //append language
    appendTagAndText(doc, entry, "dcterms:language", book.getLanguage());
    
    // append summary
    appendTagAndText(doc, entry, TAG_SUMMARY, book.getSummary());
    
    // append Source link
    if (!book.getSourceLink().isEmpty()) {
        QDomElement textLink = doc.createElement(TAG_LINK);
	    textLink.setAttribute(ATTRIBUTE_TYPE, "application/" + book.getFormat());
	    textLink.setAttribute(ATTRIBUTE_RELATION, ATTR_VALUE_ACQUISITION);
	    textLink.setAttribute(ATTRIBUTE_REFERENCE, book.getSourceLink());
	    entry.appendChild(textLink);		
	}
    
    // append Cover link
    if (!book.getCoverLink().isEmpty()) {
        QDomElement coverLink = doc.createElement(TAG_LINK);
	    coverLink.setAttribute(ATTRIBUTE_TYPE, "image");
	    coverLink.setAttribute(ATTRIBUTE_RELATION, ATTR_VALUE_COVER);
        coverLink.setAttribute(ATTRIBUTE_REFERENCE, book.getCoverLink());
	    entry.appendChild(coverLink);	
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
