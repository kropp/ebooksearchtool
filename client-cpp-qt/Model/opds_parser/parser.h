#ifndef _OPDS_PARSER_H_
#define _OPDS_PARSER_H_

#include <QVector>
#include <QIODevice>

class Catalog;
class Book;
class SearchResult;
class LinksInformation;

class OPDSParser {

public:
	OPDSParser();
	
public:
        bool parse(QIODevice* input, QVector<Book*>* data, SearchResult&);
        bool parseBooksOrCatalogs(QIODevice* input, QVector<Book*>* bookData, QVector<Catalog*>* catalogData, SearchResult& result, QString parsedServer);
        bool parseOpdsLinks(QIODevice* input, LinksInformation*);
    };

#endif //_OPDS_PARSER_H_

