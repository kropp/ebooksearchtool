#ifndef _MODEL_H_
#define _MODEL_H_

#include <string>
#include <vector>

#include "book_author.h"

class Model {

public:
	Model();

public:
	void addBook(const Book* book);
//	void addAuthor(const Author* author);
	const std::vector<const Book*> getBooks() const;

private:
	std::vector<const Book*> myBooks;
	//std::vector<const Author*> myAuthors;
};


inline const std::vector<const Book*> Model::getBooks() const {
	return myBooks;
}

#endif //_MODEL_H_
