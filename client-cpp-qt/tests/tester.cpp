#include "tester.h"

#include <QDebug>

void Tester::test () const {
    foreach (const Test* test, myTests) {
        test->run();
        qDebug() << test->getInfo();
    }
}

void Tester::addTest(Test* newTest) {
    myTests.push_back(newTest);
}
    

