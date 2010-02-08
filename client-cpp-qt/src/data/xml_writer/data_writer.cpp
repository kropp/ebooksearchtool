#include <QFile>
#include <QtXml>

#include "data_writer.h"
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
    QDomElement feed = doc.createElement("feed");
    feed.setAttribute("xmlns", "http://www.w3.org/2005/Atom");
    feed.setAttribute("xmlns:dcterms", "http://purl.org/dc/terms/");
    doc.appendChild(feed);
    const QList<const Book*>& books = data.getBooks();
    foreach (const Book* book, books) {
        QDomElement entry = doc.createElement("entry");
	    bookToDomElement(*book, doc, entry);
        feed.appendChild(entry);
    }
}

void DataWriter::bookToDomElement(const Book& book, QDomDocument& doc, QDomElement& entry) {
    // append title
    appendTagAndText(doc, entry, "title", book.getTitle());
    
    //append id
    appendTagAndText(doc, entry, "id", book.getId());

    //append authors
    const QVector<const Author*>& authors = book.getAuthors();
    foreach (const Author* author, authors) {
        QDomElement authorElement = doc.createElement("author");
        entry.appendChild(authorElement);
        appendTagAndText(doc, authorElement, "name", author->getName());
        appendTagAndText(doc, authorElement, "uri", author->getUri());
    }
   
   //append language
    appendTagAndText(doc, entry, "dcterms:language", book.getLanguage());
    
    // append summary
    appendTagAndText(doc, entry, "summary", book.getSummary());
    
    // append Source link
    QDomElement textLink = doc.createElement("link");
	textLink.setAttribute("type", "application/" + book.getFormat());
	textLink.setAttribute("href", book.getSourceLink());
	entry.appendChild(textLink);		
	
    // append Cover link
	QDomElement coverLink = doc.createElement("link");
	coverLink.setAttribute("type", "image/png");
	coverLink.setAttribute("rel", "http://opds-spec.org/cover");
    coverLink.setAttribute("href", book.getCoverLink());
	entry.appendChild(coverLink);	
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
