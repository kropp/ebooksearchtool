#include <QtXml>

#include "parser.h"

bool AtomHandler::characters (const QString& strText) {
	myStrText = strText;
	return true;
}

bool AtomHandler::endElement (const QString&, const QString&, const QString& str) {
	if (str == "title") {
		qDebug() << "Title:" << myStrText;
	} else if (str == "name") {
		qDebug() << "\tAuthor:" << myStrText << "\n";
	}
	return true;
}

AtomParser::AtomParser() {}

void AtomParser::parse(QFile& file) {
	AtomHandler handler;
	QXmlInputSource source(&file);
	QXmlSimpleReader reader;
	reader.setContentHandler(&handler);
	reader.parse(source);
}

