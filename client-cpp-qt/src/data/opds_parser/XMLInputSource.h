#ifndef _XML_INPUT_SOURCE_H_
#define _XML_INPUT_SOURCE_H_

#include <QXmlInputSource>

class XMLInputSource : public QXmlInputSource {

public:
    XMLInputSource(QIODevice* dev) : QXmlInputSource(dev) {}
    virtual ~XMLInputSource() {}

public:
   // getData(fromOffset, toOffset);
};

#endif // _XML_INPUT_SOURCE_H_
