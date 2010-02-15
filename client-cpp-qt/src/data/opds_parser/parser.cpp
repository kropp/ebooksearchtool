#include "parser.h"
#include "handler.h"
#include "XMLInputSource.h"

OPDSParser::OPDSParser() {}

void OPDSParser::parse(QIODevice* input, Data* data) {
	XMLInputSource source(input);
	//OPDSHandler handler(data, &source);
	OPDSHandler handler(data);
	QXmlSimpleReader reader;
	reader.setContentHandler(&handler);
	reader.parse(source);
}
