#include "data.h"
#include <QDebug>

Data::Data() : myTotalEntries(0) {
}

Data::~Data() {
    foreach (const Book* book, myBooks) {
        delete book;
    }
}

void Data::addBook(const Book* book) {
	myBooks.push_back(book);
}

void Data::setTotalEntries(int size) {
    myTotalEntries = size;
}
	
//void Data::addAuthor(const Author* author) {
//	myAuthors.push_back(author);
//}

