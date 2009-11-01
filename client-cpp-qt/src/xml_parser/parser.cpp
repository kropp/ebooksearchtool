#include "parser.h"
#include "handler.h"

AtomParser::AtomParser() : myNextAtomPage(0) {}

void AtomParser::parse(QFile* file, Data* data) {
	AtomHandler handler(data);
	QXmlInputSource source(file);
	QXmlSimpleReader reader;
	reader.setContentHandler(&handler);
	reader.parse(source);
	myNextAtomPage = handler.myNextAtomPage;
}

const QString* AtomParser::getNextAtomPage() const {
    return myNextAtomPage;
}

