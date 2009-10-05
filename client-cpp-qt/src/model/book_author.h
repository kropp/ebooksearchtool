#ifndef _BOOK_AUTHOR_H_
#define _BOOK_AUTHOR_H_

#include <string>
#include <list>

class Author;

class Book {

public:
	Book(std::string title, std::string language, std::string annotation) : myTitle (title),
																			myLanguage(language),
																			myAnnotation(annotation) {}

public:
	void addAuthor(const Author* author);

private:
	const std::string myTitle;
	const std::string myLanguage;
	const std::string myAnnotation;
	std::list<const Author*> myAuthors;
};


class Author {
	
public:
	Author(std::string name) : myName (name) {}

public:
	//void addBook(const Book* book);	

private:
	const std::string myName;
	//std::list<const Book*> myBooks;
};

#endif //_BOOK_AUTHOR_H_
