#include "parser.h"
#include "handler.h"
#include "XMLInputSource.h"

OPDSParser::OPDSParser() {}

bool OPDSParser::parse(QIODevice* input, Data* data, SearchResult& result) {
    if (input->bytesAvailable() == 0) {
        return false;    
    }
    XMLInputSource source(input);
	//OPDSHandler handler(data, &source);
	OPDSHandler handler(data, result);
	QXmlSimpleReader reader;
	reader.setContentHandler(&handler);
	reader.parse(source);
    return true;
}
