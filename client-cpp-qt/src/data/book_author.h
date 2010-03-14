#ifndef _BOOK_AUTHOR_H_
#define _BOOK_AUTHOR_H_

#include <QString>
#include <QVector>
#include <QMap>

class Author;

class Book {

public:
    static bool compareAuthors(const Book*, const Book*);
    static bool compareTitles(const Book*, const Book*);

public:
	Book(const QString& title, const QString& language, const QString& summary, const QString& id);
    Book();

public:

// all the getters return an empty string 
// if the book hasn't such attribute

    const QString& getTitle() const;
	const QVector<const Author*>& getAuthors() const;
	const QString& getLanguage() const;
	const QString& getSummary() const;
	const QString& getId() const;
	const QMap<QString, QString>& getSourceLinks() const;
    const QString getSourceLink(const QString& format) const;
    QString getSourceLink() const;
    const QString& getCoverLink() const;	
    const QString& getFormat() const;	
    const QString& getContent() const;
    const QString& getUpdated() const;
    const QString& getIssued() const;
    const QString& getPublisher() const;
    const QString& getRights() const;
    const QVector<QString>& getCategories() const;

    void setTitle(const QString& title);
    void setLanguage(const QString& lang);
    void setSummary(const QString& summary);
    void setId(const QString& id);
    void addAuthor(const Author* author);
	void addSourceLink(const QString& format, const QString& link);
    void setCoverLink(const QString& path);
    void setContent(const QString& content);
    void setPublisher(const QString& publisher);
    void setUpdated(const QString& updated);
    void setIssued(const QString& issued);
    void setRights(const QString& rights);
    void addCategory(const QString& category);

private:
	QString myTitle;
	QString myLanguage;
    QString mySummary;
	QString myId;
    QVector<const Author*> myAuthors;
    QString myPublisher;
    QString myUpdated;
    QString myIssued;
    QString myContent;
    QString myRights;
    QVector<QString> myCategories; 
	QString myCoverLink;
    QMap<QString, QString> mySourceLinks; // format -> link

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

inline const QString& Book::getUpdated() const {
    return myUpdated;
}

inline const QString& Book::getIssued() const {
    return myIssued;
}

inline const QString& Book::getPublisher() const {
    return myPublisher;
}
inline const QString& Book::getRights() const {
    return myRights;
}

inline const QVector<QString>& Book::getCategories() const {
    return myCategories;
}

inline const QString& Book::getCoverLink() const {
    return myCoverLink;
}

inline const QString& Book::getContent() const {
    return myContent;
}

#endif //_BOOK_AUTHOR_H_
