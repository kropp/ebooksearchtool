#include <QtXml>

#include <iostream>
#include "parser.h"

bool AtomHandler::characters (const QString& strText) {
	myStrText = strText;
	return true;
}

bool AtomHandler::endElement (const QString&, const QString&, const QString& str) {
	if (str == "title") {
		myBuffer->append(myStrText);
		myBuffer->append(" 	 ");
	} else if (str == "name") {
		myBuffer->append(myStrText);
		myBuffer->append("\n");
	}
	return true;
}

AtomParser::AtomParser() {}

void AtomParser::parse(QFile& file) {
	AtomHandler handler(myOutputBuffer);
	QXmlInputSource source(&file);
	QXmlSimpleReader reader;
	reader.setContentHandler(&handler);
	reader.parse(source);
}

void AtomParser::setOutput(QByteArray* buffer) {
	myOutputBuffer = buffer;
}

