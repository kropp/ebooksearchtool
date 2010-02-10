#ifndef _BOOK_AUTHOR_H_
#define _BOOK_AUTHOR_H_

#include <QString>
#include <QVector>
#include <QMap>

class Author;

class Book {

public:
	Book(const QString& title, const QString& language, const QString& summary, const QString& id) : myTitle (title),
                           myLanguage(language),
					       mySummary(summary),
						   myId(id) {}

public:
	const QString& getTitle() const;
	const QVector<const Author*>& getAuthors() const;
	const QString& getLanguage() const;
	const QString& getSummary() const;
	const QString& getId() const;
	const QMap<const QString, QString>& getSourceLinks() const;
//    const QString getSourceLink(const QString& format) const;
    const QString& getSourceLink() const;
    const QString& getCoverLink() const;	
    const QString& getFormat() const;	

	void addAuthor(const Author* author);
	//void addSourceLink(const QString& format, QString link);
	void setSourceLink(const QString& format, const QString& link);
    void setCoverLink(const QString& path);

private:
	const QString myTitle;
	const QString myLanguage;
	const QString mySummary;
	QVector<const Author*> myAuthors;
	const QString myId;
	QString myCoverLink;
    QString myFormat;
    QString mySourceLink;
   // QMap<const QString, QString> mySourceLinks; // format -> link
};


class Author {
	
public:
	Author(QString name, QString uri) : myName (name), myUri(uri) {}

public:
	//void addBook(const Book* book);	
	const QString& getName() const;
	const QString& getUri() const;

private:
	const QString myName;
	const QString myUri;
	//std::list<const Book*> myBooks;
};


inline const QString& Book::getTitle() const {
	return myTitle;
}

inline const QString& Book::getLanguage() const {
	return myLanguage;
}

inline const QString& Book::getSummary() const {
	return mySummary;
}

inline const QString& Author::getName() const {
	return myName;
}

inline const QString& Book::getId() const {
	return myId;
}

inline const QString& Author::getUri() const {
	return myUri;
}

inline const QString& Book::getCoverLink() const {
    return myCoverLink;
}

inline void Book::setCoverLink(const QString& path) {
    myCoverLink = path;
}

inline const QVector<const Author*>& Book::getAuthors() const {
    return myAuthors;
}

inline void Book::setSourceLink(const QString& format, const QString& link) {
    myFormat = format;
    mySourceLink = link;
}

inline const QString& Book::getSourceLink() const {
    return mySourceLink;
}
    
inline const QString& Book::getFormat() const {
    return myFormat;
}	
#endif //_BOOK_AUTHOR_H_
