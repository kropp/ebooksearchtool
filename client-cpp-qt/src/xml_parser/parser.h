#ifndef _PARSER_H_
#define _PARSER_H_

class QFile;
class Data;
class QString;

class AtomParser {

public:
	AtomParser();
	
public:
	void parse(QFile* file, Data* data);
	const QString* getNextAtomPage() const;

private:
    QString* myNextAtomPage;
};

#endif //_PARSER_H_

