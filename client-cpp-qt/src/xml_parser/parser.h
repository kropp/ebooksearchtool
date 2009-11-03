#ifndef _PARSER_H_
#define _PARSER_H_

class QIODevice;
class Data;
class QString;

class AtomParser {

public:
	AtomParser();
	
public:
	void parse(QIODevice* input, Data* data);
	const QString* getNextAtomPage() const;

private:
    QString* myNextAtomPage;
};

#endif //_PARSER_H_

