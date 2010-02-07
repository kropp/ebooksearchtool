#include "book_author.h"

void Book::addAuthor(const Author* author) {
	myAuthors.push_back(author);
}

void Book::setSourceLink(const QString& link, const QString& format) {
	mySourceLink = link;
    myFormat = format;
}


//void Author::addBook(const Book* book) {
//	myBooks.push_back(book);
//}	

