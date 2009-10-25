#ifndef _PARSER_H_
#define _PARSER_H_

class QFile;
class Model;
class QString;

class AtomParser {

public:
	AtomParser();
	
public:
	void parse(QFile* file, Model* model);
	const QString* getNextAtomPage() const;

private:
    QString* myNextAtomPage;
};

#endif //_PARSER_H_

