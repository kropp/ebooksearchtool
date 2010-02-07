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
};

#endif //_PARSER_H_

