#ifndef _DATA_H_
#define _DATA_H_

#include <QList>

#include "book_author.h"

class Data {

public:
	Data();

public:
    const QList<const Book*>& getBooks() const;
    size_t getSize() const;
    size_t getTotalEntries() const;

    void addBook(const Book* book);
    void setTotalEntries(int size);

private:
	  QList<const Book*> myBooks;
      size_t myTotalEntries;

private:
    Data(const Data& otherData);
    Data& operator= (const Data& otherData);
};

inline size_t Data::getSize() const {
	return myBooks.size();
}

inline size_t Data::getTotalEntries() const {
    return myTotalEntries;
}

inline const QList<const Book*>& Data::getBooks() const {
    return myBooks;
}

#endif //_DATA_H_
