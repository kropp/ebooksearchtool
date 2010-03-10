#ifndef _OPDS_PARSER_H_
#define _OPDS_PARSER_H_

class QIODevice;
class Data;
class QString;
class SearchResult;

class OPDSParser {

public:
	OPDSParser();
	
public:
	bool parse(QIODevice* input, Data* data);
	bool parse(QIODevice* input, Data* data, SearchResult&);
};

#endif //_OPDS_PARSER_H_

