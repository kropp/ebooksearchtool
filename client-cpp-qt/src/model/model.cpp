#include "model.h"

Model::Model() : myTotalEntries(0) {}

void Model::addBook(const Book* book) {
	myBooks.push_back(book);
}

void Model::setTotalEntries(int size) {
    myTotalEntries = size;
}
	
//void Model::addAuthor(const Author* author) {
//	myAuthors.push_back(author);
//}

