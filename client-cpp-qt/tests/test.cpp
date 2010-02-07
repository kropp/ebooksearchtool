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

//const QString XMLParserTest::OPDSFilesPath = "OPDS_files";

XMLParserTest::XMLParserTest(const QString& info, const QString& fileName) : myFileName (fileName) {
    myInfo = info;
    myInfo += "  input file: ";
    myInfo += fileName;
}
 
bool XMLParserTest::run() const {
    qDebug() << "Test::run started";
    QFile input(myFileName);
    qDebug() << "Test::run want to open file";// << input.fileName();

    if (!input.open(QIODevice::ReadOnly)) {
        qDebug() << "can't open file";// << input.fileName();
        return false;
    } else {
        qDebug() << "Test::run file opened";
    }
    
    Data* data = new Data();

    AtomParser parser;
    parser.parse(&input, data);
    qDebug() << "Test::run parser finished";
    
    input.close();
    
    qDebug() << "input closed";
    
   // QString outFileName = myFileName;

    qDebug() << "want to prepend out";
    //outFileName.prepend("out_");
    
    qDebug() << "want to create out-file";
    QFile out("out");
    qDebug() << "want to open file";
    out.open(QIODevice::WriteOnly);
    qDebug() << "file opened";
    DataWriter writer;
    qDebug() << "Test::run writer will start";
    writer.write (&out, *data);
    qDebug() << "Test::run writer finished";
    out.close();

    return false;
}

