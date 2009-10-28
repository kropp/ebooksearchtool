#include "book_author.h"

void Book::addAuthor(const Author* author) {
	myAuthors.push_back(author);
}

void Book::setLink(const std::string& link) {
	myLink = link;
}
//void Author::addBook(const Book* book) {
//	myBooks.push_back(book);
//}	

