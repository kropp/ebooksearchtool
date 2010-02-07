#ifndef _TESTER_H_
#define _TESTER_H_

#include <QVector>
#include "test.h"


class Tester {

public:
	void test() const; //выводит на консоль результаты всех тестов
    void addTest(Test* test);

private:
    QVector<Test*> myTests;
};

#endif //_TESTER_H_
