#ifndef _DATA_WRITER_H_
#define _DATA_WRITER_H_

class QFile;
class Data;
class Book;

class DataWriter {

public:
    DataWriter();

public:
    void write(QFile* file, const Data& data);

private:
    void bookToDomElement(const Book& book, QDomDocument& doc, QDomElement& element);
    void dataToDomDocument(const Data& data, QDomDocument& doc);
};

#endif  // _DATA_WRITER_H_
