#include "parser.h"
#include "handler.h"

AtomParser::AtomParser() {}

void AtomParser::parse(QIODevice* input, Data* data) {
	OPDSHandler handler(data);
	QXmlInputSource source(input);
	QXmlSimpleReader reader;
	reader.setContentHandler(&handler);
	reader.parse(source);
}
