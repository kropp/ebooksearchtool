#include "book_author.h"

void Book::addAuthor(const Author* author) {
	myAuthors.push_back(author);
}

const QString& Book::getCoverLink() const {
    return myCoverLink;
}

const QString& Book::getContent() const {
    return myContent;
}

void Book::setCoverLink(const QString& path) {
    myCoverLink = path;
}

const QVector<const Author*>& Book::getAuthors() const {
    return myAuthors;
}

void Book::setSourceLink(const QString& format, const QString& link) {
    myFormat = format;
    mySourceLink = link;
}

const QString& Book::getSourceLink() const {
    return mySourceLink;
}
    
const QString& Book::getFormat() const {
    return myFormat;
}	

void Book::setContent(const QString& content) {
    myContent = content;
}

const QString& Author::getUri() const {
	return myUri;
}

/*void Book::addSourceLink(const QString& format, QString link) {
    mySourceLinks.insert(format, link);
}

const QString Book::getSourceLink(const QString& format) const {
    QMap<const QString, QString>::const_iterator i = mySourceLinks.find(format);
    if (i == mySourceLinks.end()) {
        return QString();
    } else {
        return i.value();    
    }
}

const QMap<const QString, QString>& Book::getSourceLinks() const {
    return mySourceLinks;
}*/
