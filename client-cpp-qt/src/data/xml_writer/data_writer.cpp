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


    /*  TODO append all the authors
    QDomElement author = doc.createElement("author");
    entry.appendChild(author);
    QDomElement name = doc.createElement("name");
    author.appendChild(name);
    QDomText nameText = doc.createTextNode(book.getAu);
    name.appendChild(nameText);
*/
    // append summary
    QDomElement summary= doc.createElement("summary");
    entry.appendChild(summary);
    QDomText summaryText = doc.createTextNode(book.getSummary());
    summary.appendChild(summaryText);

	QDomElement textLink = doc.createElement("link");
	textLink.setAttribute("type", "application/" + book.getFormat());
	textLink.setAttribute("href", book.getSourceLink());
	entry.appendChild(textLink);		
	
	QDomElement coverLink = doc.createElement("link");
	coverLink.setAttribute("type", "image/png");
	coverLink.setAttribute("href", book.getCoverLink());
	entry.appendChild(coverLink);	
}

void DataWriter::appendTagAndText(QDomDocument& doc, QDomElement& parentElement, const QString& tag, const QString& text) {
    QDomElement newElement = doc.createElement(tag);
    parentElement.appendChild(newElement);
    QDomText domText = doc.createTextNode(text);
    newElement.appendChild(domText);
}
