#ifndef _BOOK_AUTHOR_H_
#define _BOOK_AUTHOR_H_

#include <QString>
#include <QVector>
#include <QMap>

class Author;

class Book {

public:
	Book(const QString& title, const QString& language, const QString& summary, const QString& id);
    Book();

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
    const QString& getContent() const;

    void setTitle(const QString& title);
    void setLanguage(const QString& lang);
    void setSummary(const QString& summary);
    void setId(const QString& id);
    void addAuthor(const Author* author);
	//void addSourceLink(const QString& format, QString link);
	void setSourceLink(const QString& format, const QString& link);
    void setCoverLink(const QString& path);
    void setContent(const QString& content);

private:
	QString myTitle;
	QString myLanguage;
	QString mySummary;
    QString myContent;
    QVector<const Author*> myAuthors;
	QString myId;
	QString myCoverLink;
    QString myFormat;
    QString mySourceLink;
   
    // QMap<const QString, QString> mySourceLinks; // format -> link

private:
    Book(const Book& other);
    Book& operator= (const Book& other);
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

#endif //_BOOK_AUTHOR_H_
