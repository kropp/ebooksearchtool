#include <QCoreApplication>
#include <QtXml>
#include <QFile>

#include "../src/data/data.h"
#include "../src/data/xml_parser/parser.h"
#include "../src/data/xml_parser/handler.h"
#include "../src/data/xml_writer/data_writer.h"

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
// write data to the new file
    return 0;
}
