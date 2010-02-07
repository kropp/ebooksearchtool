#include <QCoreApplication>
#include "tester.h"

#include <QtXml>
#include <QFile>

#include "../src/data/data.h"
#include "../src/data/xml_parser/parser.h"
#include "../src/data/xml_parser/handler.h"
#include "../src/data/xml_writer/data_writer.h"


#include <QDebug>

int main() {

    QFile input("OPDS_files/feedbooks_Doyle.atom");
    input.open(QIODevice::ReadOnly);
           
    Data* data = new Data();
                
    AtomParser parser;
    parser.parse(&input, data);
                            
    input.close();
             
    QFile out("out.xml");
    out.open(QIODevice::WriteOnly);
    DataWriter writer;
    writer.write (&out, *data);
    out.close();
                                            /*    Tester* tester = new Tester;
    XMLParserTest* test =  new XMLParserTest("xml_parser test", "OPDS_files/feedbooks_Doyle.atom");
    tester->addTest(test);

    qDebug() << "test Added";
    tester->test();
*/
    return 0;
}
