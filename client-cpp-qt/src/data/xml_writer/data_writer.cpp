#include <QFile>
#include <QtXml>

#include "data_writer.h"
#include "../data.h"

DataWriter::DataWriter() {}

void DataWriter::write(QFile* file, const Data& data) {
	QDomDocument* doc = new QDomDocument();
    dataToDomDocument(data, *doc);
	QTextStream out(file);//, QIODevice::WriteOnly);
	doc->save(out, 4);
    delete doc;
}

void DataWriter::bookToDomElement(const Book& book, QDomDocument& doc, QDomElement& entry) {

    QDomElement title = doc.createElement("title");
    entry.appendChild(title);
    QDomText titleText = doc.createTextNode(book.getTitle());
    title.appendChild(titleText);
    /*  TODO append all the authors
    QDomElement author = doc.createElement("author");
    entry.appendChild(author);
    QDomElement name = doc.createElement("name");
    author.appendChild(name);
    QDomText nameText = doc.createTextNode(book.getAu);
    name.appendChild(nameText);
*/
    QDomElement summary= doc.createElement("summary");
    entry.appendChild(summary);
    QDomText summaryText = doc.createTextNode(book.getSummary());
    summary.appendChild(summaryText);

	QDomElement textLink = doc.createElement("link");
	textLink.setAttribute("type", "application/epub+zip");
	textLink.setAttribute("href", book.getSourceLink());
	entry.appendChild(textLink);		
	
	QDomElement coverLink = doc.createElement("link");
	coverLink.setAttribute("type", "image/png");
	coverLink.setAttribute("href", book.getCoverLink());
	entry.appendChild(coverLink);	
}

void DataWriter::dataToDomDocument(const Data& data, QDomDocument& doc) {
    // TODO create <feed> element
    const QList<const Book*>& books = data.getBooks();
    foreach (const Book* book, books) {
        QDomElement entry = doc.createElement("entry");
	    bookToDomElement(*book, doc, entry);
        doc.appendChild(entry);
    }
}
