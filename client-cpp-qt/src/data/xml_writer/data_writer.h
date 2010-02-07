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
    void bookToXml(const Book& book);
};

#endif  // _DATA_WRITER_H_
