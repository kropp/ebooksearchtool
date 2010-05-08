#include "book.h"

#include <QDebug>

Book::Book(const QString& title,
           const QString& language,
           const QString& summary,
           const QString& id) : myTitle (title),
                                myLanguage(language),
                                mySummary(summary),
                                myId(id) {}


Book::Book() {}


bool Book::compareAuthors(const Book* book1, const Book* book2) {
    return book1->getAuthors()[0]->getName() < book2->getAuthors()[0]->getName();
}

bool Book::compareTitles(const Book* book1, const Book* book2) {
    return book1->getTitle() < book2->getTitle();
}

void Book::setServerName(const QString& server) {
    myServerName = server;
}

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

void Book::setCoverLink(const QString& path) {
    myCoverLink = path;
}

const QVector<const Author*>& Book::getAuthors() const {
    return myAuthors;
}

void Book::setContent(const QString& content) {
    myContent = content;
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

void Book::setPublisher(const QString& publisher) {
    myPublisher = publisher;
}
void Book::setRights(const QString& rights) {
    myRights = rights;
}

void Book::addCategory(const QString& category) {
    myCategories.push_back(category);
}

