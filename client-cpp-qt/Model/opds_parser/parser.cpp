#include "parser.h"
#include "handler.h"
#include "../catalog.h"
#include "../linksinformation.h"

#include <QXmlInputSource>

OPDSParser::OPDSParser() {}

bool OPDSParser::parse(QIODevice* input, QVector<Book*>* data, SearchResult& result) {

    if (input->bytesAvailable() == 0) {
        return false;    
    }

    QXmlInputSource source(input);
    OPDSHandler handler(data, result);
    QXmlSimpleReader reader;
    reader.setContentHandler(&handler);
    reader.parse(source);

    return true;
}

bool OPDSParser::parseBooksOrCatalogs
(
        QIODevice* input,
        QVector<Book*>* bookData,
        QVector<Catalog*>* catalogData,
        SearchResult& result,
        QString parsedServer
)
{

    if (input->bytesAvailable() == 0) {
        return false;
    }

    QXmlInputSource source(input);
    OPDSHandler handler(bookData, catalogData, parsedServer, result);
    QXmlSimpleReader reader;
    reader.setContentHandler(&handler);
    reader.parse(source);

    return true;
}


bool OPDSParser::parseOpdsLinks(QIODevice* input, LinksInformation* linksInfo) {
    if (input->bytesAvailable() == 0) {
        return false;
    }

    QXmlInputSource source(input);
    OPDSHandler handler(linksInfo);
    QXmlSimpleReader reader;
    reader.setContentHandler(&handler);
    reader.parse(source);

    return true;
}
