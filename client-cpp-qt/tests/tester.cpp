#include "tester.h"

#include <QDebug>

void Tester::test () const {
    qDebug() << "Tester::test started";
    foreach (const Test* test, myTests) {
        qDebug() << "Test::run will start";
        test->run();
        qDebug() << test->getInfo();
    }
}

void Tester::addTest(Test* newTest) {
    myTests.push_back(newTest);
}
    

