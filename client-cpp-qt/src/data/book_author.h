#ifndef _BOOK_AUTHOR_H_
#define _BOOK_AUTHOR_H_

#include <string>
#include <vector>

class Author;

class Book {

public:
	Book(const std::string& title, const std::string& language, const std::string& summary, const std::string& uri) : myTitle (title),
																			myLanguage(language),
																			mySummary(summary),
																			myUri(uri) {}

public:
	const std::string& getTitle() const;
	const Author* getAuthor() const;
	const std::string& getLanguage() const;
	const std::string& getSummary() const;
	const std::string& getUri() const;
	const std::string& getLink() const;
    const std::string& getCoverPath() const;	

	void addAuthor(const Author* author);
	void setLink(const std::string& link);
	void setCoverPath(const std::string& path);

private:
	const std::string myTitle;
	const std::string myLanguage;
	const std::string mySummary;
	std::vector<const Author*> myAuthors;
	const std::string myUri;
	std::string myLink;
	std::string myCoverPath;
};


class Author {
	
public:
	Author(std::string name, std::string uri) : myName (name), myUri(uri) {}

public:
	//void addBook(const Book* book);	
	const std::string& getName() const;
	const std::string& getUri() const;

private:
	const std::string myName;
	const std::string myUri;
	//std::list<const Book*> myBooks;
};


inline const std::string& Book::getTitle() const {
	return myTitle;
}

inline const Author* Book::getAuthor() const {
	if (myAuthors.empty()) {
		return 0;
	}
	return myAuthors[0];
}

inline const std::string& Book::getLanguage() const {
	return myLanguage;
}

inline const std::string& Book::getSummary() const {
	return mySummary;
}

inline const std::string& Author::getName() const {
	return myName;
}

inline const std::string& Book::getUri() const {
	return myUri;
}

inline const std::string& Book::getLink() const {
	return myLink;
}


inline const std::string& Author::getUri() const {
	return myUri;
}

inline const std::string& Book::getCoverPath() const {
    return myCoverPath;
}

inline void Book::setCoverPath(const std::string& path) {
    myCoverPath = path;
}


#endif //_BOOK_AUTHOR_H_
