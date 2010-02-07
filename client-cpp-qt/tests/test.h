#ifndef _TEST_H_
#define _TEST_H_

#include <QString>

class Test {

public:
    virtual ~Test() {} 

public:
    const QString& getInfo() const; 
    virtual bool run() const = 0; 

protected:
    QString myInfo;
};

class XMLParserTest : public Test {

public:
    static const QString OPDSFilesPath;

public:
    XMLParserTest(const QString& info, const QString& fileName); 
    virtual ~XMLParserTest() {}

    const QString& getInfo() const; 
    bool run() const;

private:
    const QString& myFileName;
};

#endif //_TEST_H_
