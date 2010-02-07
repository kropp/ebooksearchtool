#include <QCoreApplication>
#include "tester.h"

#include <QDebug>

int main() {

    Tester tester;
    XMLParserTest test1("xml_parser test", "OPDS_files/feedbooks_Doyle.atom");
    XMLParserTest test2("xml_parser test", "OPDS_files/feedbooks_Pushkin.atom");
    tester.addTest(&test1);
    tester.addTest(&test2);

    tester.test();

    return 0;
}
