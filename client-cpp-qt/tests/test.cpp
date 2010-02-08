#include <QtXml>
#include <QFile>

#include "../src/data/data.h"
#include "../src/data/xml_parser/parser.h"
#include "../src/data/xml_parser/handler.h"
#include "../src/data/xml_writer/data_writer.h"

#include "test.h"

const QString& Test::getInfo() const {
    return myInfo;
}

XMLParserTest::XMLParserTest(const QString& info, const QString& fileName) : myFileName(fileName) {
    myInfo = info;
    myInfo += "  input file: ";
    myInfo += fileName;
}
 
bool XMLParserTest::run() const {
    QFile input(myFileName); 
    input.open(QIODevice::ReadOnly);
           
    Data* data = new Data();
                
    AtomParser parser;
    parser.parse(&input, data);
                            
    input.close();
    
    QString outFileName(myFileName);
    outFileName.remove(".atom");
    outFileName.remove(".xml");
    QFile out(outFileName.append("_out.xml"));
    out.open(QIODevice::WriteOnly);
    DataWriter writer;
    writer.write (&out, *data);
    out.close();
 
    return false;
}

