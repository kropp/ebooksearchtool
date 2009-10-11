#ifndef _BOOK_AUTHOR_H_
#define _BOOK_AUTHOR_H_

#include <string>
#include <vector>

class Author;

class Book {

public:
	Book(std::string title, std::string language, std::string summary, std::string uri) : myTitle (title),
																			myLanguage(language),
																			mySummary(summary),
																			myUri(uri) {}

public:
	void addAuthor(const Author* author);
	
	std::string getTitle() const;
	const Author* getAuthor() const;
	std::string getLanguage() const;
	std::string getSummary() const;
	std::string getUri() const;
	std::string getLink() const;
	
	void setLink(std::string link);

private:
	const std::string myTitle;
	const std::string myLanguage;
	const std::string mySummary;
	std::vector<const Author*> myAuthors;
	const std::string myUri;
	std::string myLink;
};


class Author {
	
public:
	Author(std::string name, std::string uri) : myName (name), myUri(uri) {}

public:
	//void addBook(const Book* book);	
	std::string getName() const;
	std::string getUri() const;

private:
	const std::string myName;
	const std::string myUri;
	//std::list<const Book*> myBooks;
};


inline std::string Book::getTitle() const {
	return myTitle;
}

inline const Author* Book::getAuthor() const {
	if (myAuthors.empty()) {
		return 0;
	}
	return myAuthors[0];
}

inline std::string Book::getLanguage() const {
	return myLanguage;
}

inline std::string Book::getSummary() const {
	return mySummary;
}

inline std::string Author::getName() const {
	return myName;
}

inline std::string Book::getUri() const {
	return myUri;
}

inline std::string Book::getLink() const {
	return myLink;
}


inline std::string Author::getUri() const {
	return myUri;
}

#endif //_BOOK_AUTHOR_H_
