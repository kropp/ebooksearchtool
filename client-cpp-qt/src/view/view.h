#ifndef _VIEW_H_
#define _VIEW_H_

#include "../data/data.h"
#include <../network/httpconnection.h>

class Data; 

class View : public QWidget {

public:
    View(QWidget* parent, Data* data);

public:
    Data* getData() const;
    void resetData(Data* data);

private:
    Data* myData;
};

inline Data* View::getData() const {
    return myData;
}

#endif //_VIEW_H_
