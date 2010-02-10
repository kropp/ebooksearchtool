#include "book_author.h"

void Book::addAuthor(const Author* author) {
	myAuthors.push_back(author);
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
