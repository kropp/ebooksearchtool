#include <QtXml>

class IdAtomParser : public QXmlDefaultHandler {
private:
	QString myStrText;
	
public:
	bool characters (const QString& strText);
	bool endElement (const QString&, const QString&, const QString& str);
};

/////////////////////////////////

int main() {
	IdAtomParser handler;
	QFile file("id.atom");
	QXmlInputSource source(&file);
	QXmlSimpleReader reader;
	reader.setContentHandler(&handler);
	reader.parse(source);
	return 0;
}
///////////////////////////////////


bool IdAtomParser::characters (const QString& strText) {
	myStrText = strText;
	return true;
}

bool IdAtomParser::endElement (const QString&, const QString&, const QString& str) {
	if (str == "title") {
		qDebug() << "Title:" << myStrText;
	} else if (str == "name") {
		qDebug() << "\tAuthor:" << myStrText << "\n";
	}
	return true;
}


