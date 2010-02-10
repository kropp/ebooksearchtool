#ifndef _DATA_WRITER_H_
#define _DATA_WRITER_H_

#include "../opds_parser/opds_constants.h"

class QFile;
class Data;
class Book;

class DataWriter : public OPDSConstants {

public:
    DataWriter();

public:
    void write(QFile* file, const Data& data);

private:
    void bookToDomElement(const Book& book, QDomDocument& doc, QDomElement& element);
    void dataToDomDocument(const Data& data, QDomDocument& doc);
    void appendTagAndText(QDomDocument& doc, QDomElement& parentElement, const QString& tag, const QString& text);
};

#endif  // _DATA_WRITER_H_
