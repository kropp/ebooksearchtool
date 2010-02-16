#include "book_author.h"

#include <QDebug>

Book::Book(const QString& title,
           const QString& language,
           const QString& summary,
           const QString& id) : myTitle (title),
                                myLanguage(language),
					            mySummary(summary),
						        myId(id) {}


Book::Book() {}


void Book::setTitle(const QString& title) {
    myTitle = title;
}

void Book::setLanguage(const QString& lang) {
    myLanguage = lang;
}

void Book::setSummary(const QString& summary) {
    mySummary = summary;
}

void Book::setId(const QString& id) {
    myId = id;
}

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

void Book::setContent(const QString& content) {
    myContent = content;
}

const QString& Author::getUri() const {
	return myUri;
}

void Book::addSourceLink(const QString& format, const QString& link) {
    mySourceLinks.insert(format, link);
}

QString Book::getSourceLink() const {
    qDebug() << " Book::getSourceLink default - return pdf-link !!!!";
    return getSourceLink("pdf");
}

const QString Book::getSourceLink(const QString& format) const {
    QMap<QString, QString>::const_iterator i = mySourceLinks.find(format);
    if (i == mySourceLinks.constEnd()) {
        return QString();
    } else {
        return i.value();    
    }
}

const QMap<QString, QString>& Book::getSourceLinks() const {
    return mySourceLinks;
}


void Book::setUpdated(const QString& updated) {
    myUpdated = updated;
}

void Book::setIssued(const QString& issued) {
    myIssued = issued;
}

void Book::setRights(const QString& rights) {
    myRights = rights;
}

void Book::addCategory(const QString& category) {
    myCategories.push_back(category);
}
