#ifndef _BOOK_AUTHOR_H_
#define _BOOK_AUTHOR_H_

#include <string>
#include <list>

class Author;

class Book {

public:
	Book(std::string title, std::string language, std::string summary) : myTitle (title),
																			myLanguage(language),
																			mySummary(summary) {}

public:
	void addAuthor(const Author* author);
	
	std::string getTitle() const;

private:
	const std::string myTitle;
	const std::string myLanguage;
	const std::string mySummary;
	std::list<const Author*> myAuthors;
	//TODO id to add
};


class Author {
	
public:
	Author(std::string name) : myName (name) {}

public:
	//void addBook(const Book* book);	

private:
	const std::string myName;
	//const int myId;
	//std::list<const Book*> myBooks;
};

inline std::string Book::getTitle() const {
	return myTitle;
}


#endif //_BOOK_AUTHOR_H_
