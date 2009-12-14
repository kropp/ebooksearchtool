#ifndef _DATA_H_
#define _DATA_H_

#include <string>
#include <vector>

#include "book_author.h"

class Data {

public:
	Data();

public:
    const Book* getBook(size_t index) const;
    size_t getSize() const;
    size_t getTotalEntries() const;

    void addBook(const Book* book);
    void setTotalEntries(int size);

private:
	  std::vector<const Book*> myBooks;
	  size_t myTotalEntries; // сколько ожидаю книг, пока еще не все закачались
};

inline const Book* Data::getBook(size_t index) const {
    return (index < myBooks.size()) ? myBooks[index] : 0;
}

inline size_t Data::getSize() const {
	return myBooks.size();
}

inline size_t Data::getTotalEntries() const {
    return myTotalEntries;
}

#endif //_DATA_H_
