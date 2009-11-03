#include "parser.h"
#include "handler.h"

AtomParser::AtomParser() : myNextAtomPage(0) {}

void AtomParser::parse(QIODevice* input, Data* data) {
	AtomHandler handler(data);
	QXmlInputSource source(input);
	QXmlSimpleReader reader;
	reader.setContentHandler(&handler);
	reader.parse(source);
	myNextAtomPage = handler.myNextAtomPage;
}

const QString* AtomParser::getNextAtomPage() const {
    return myNextAtomPage;
}

