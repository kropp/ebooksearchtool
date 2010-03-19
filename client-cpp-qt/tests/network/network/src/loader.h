#ifndef _loader_h_
#define _loader_h_

#include <QObject>
#include <QFile>

#include "../../../src/network/networkmanager.h"

class Loader : public QObject { 
    
    Q_OBJECT

public:
    Loader();
    ~Loader();

public:
    void load(QString request, QIODevice* file);

private slots:
    bool requestFinished(int id, bool error); 

private:
    int myRequestId;
};

#endif //_loader_h_
