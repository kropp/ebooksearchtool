#include <QtXml>

#include "parser.h"

bool AtomHandler::characters (const QString& strText) {
	myStrText = strText;
	return true;
}

bool AtomHandler::startElement (const QString& , const QString& , const QString& name, const QXmlAttributes& ) {
	if (name == "entry") {
		myIsEntry = true;
	}
	return true;
}

bool AtomHandler::endElement (const QString&, const QString&, const QString& str) {
	if ((str == "title") && (myIsEntry)) {
		myBuffer->append(myStrText);
		myBuffer->append(" 	 ");
	} else if ((str == "name") && (myIsEntry)) {
		myBuffer->append(myStrText);
		myBuffer->append("\n");
	} else if (str == "entry") {
		myIsEntry = false;	
	}
	return true;
}

AtomParser::AtomParser() {}

void AtomParser::parse(QFile* file) {
	AtomHandler handler(myOutputBuffer);
	QXmlInputSource source(file);
	QXmlSimpleReader reader;
	reader.setContentHandler(&handler);
	reader.parse(source);
}

void AtomParser::setOutput(QByteArray* buffer) {
	myOutputBuffer = buffer;
}

