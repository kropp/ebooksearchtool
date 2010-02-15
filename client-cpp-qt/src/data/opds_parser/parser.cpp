#include "parser.h"
#include "handler.h"
#include "XMLInputSource.h"

OPDSParser::OPDSParser() {}

void OPDSParser::parse(QIODevice* input, Data* data) {
	OPDSHandler handler(data);
	XMLInputSource source(input);
	QXmlSimpleReader reader;
	reader.setContentHandler(&handler);
	reader.parse(source);
}
