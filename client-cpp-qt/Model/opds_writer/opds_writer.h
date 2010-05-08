#ifndef _DATA_WRITER_H_
#define _DATA_WRITER_H_

#include "../opds_parser/opds_constants.h"

#include <QDomDocument>

class QFile;
class Book;

class DataWriter : public OPDSConstants {

public:
    DataWriter();

public:
    void write(QFile* file, const QVector<Book*>& data);

private:
    void bookToDomElement(const Book& book, QDomDocument& doc, QDomElement& element);
    void dataToDomDocument(const QVector<Book*>& data, QDomDocument& doc);
    
    void appendTagAndText(QDomDocument& doc, QDomElement& parentElement, const QString& tag, const QString& text);

    void appendCoverLink(QDomDocument& doc, QDomElement& entry, const Book& book);
    void appendSourceLinks(QDomDocument& doc, QDomElement& entry, const Book& book);
};

#endif  // _DATA_WRITER_H_
