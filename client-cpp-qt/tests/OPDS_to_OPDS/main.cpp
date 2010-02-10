// test for OPDS parser
// deserialization Data from input file
// serialization Data to output file
#include <QtXml>
#include <QDebug>
#include <QFile>

#include "../../src/data/data.h"
#include "../../src/data/xml_parser/parser.h"
#include "../../src/data/xml_parser/handler.h"
#include "../../src/data/xml_writer/data_writer.h"

void opdsToOpds(QFile& in, QFile& out);

int main(int argc, char** argv) {
    // check arguments
    if (argc < 2) {
        qDebug() << "Usage: ./OPDS_to_OPDS [input file] [output file]";
        return 0;
    } else if (argc == 2) {
        qDebug() << "you use default output file 'out.xml'";
//        qDebug() << "for setting your own output file name" <<
  //                  "Usage: ./OPDS_to_OPDS [input file] [output file]";
    }
    
    // create input file, check existing
    QFile input(argv[1]);
    if (!QFile::exists(input.fileName())) {
        qDebug() << "file " << input.fileName() << "doesn't exist!";
        return 0;
    }

    // create output file
    QString outFileName;
    if (argc > 2) {
        outFileName = argv[2];
    } else {
        outFileName = "out.xml";
    }
    QFile output(outFileName);
   
   // deserialize data from input file
   // and serialze data to output
    opdsToOpds(input, output);

    return 0;
}

void opdsToOpds(QFile& input, QFile& out) {
    qDebug() << "opds_to_opds";
    
    // parse input file
    input.open(QIODevice::ReadOnly);
    Data* data = new Data();
    
    AtomParser parser;
    parser.parse(&input, data);
                            
    input.close();
   
   // write to output file
    out.open(QIODevice::WriteOnly);
    DataWriter writer;
    writer.write (&out, *data);
    out.close();
}
