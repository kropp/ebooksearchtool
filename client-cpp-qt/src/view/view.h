#ifndef _VIEW_H_
#define _VIEW_H_

#include <QWidget>

#include "../data/data.h"
#include "../view/bookwidget.h"
#include <QWidget>
#include <QVBoxLayout>

class Data; 

class View : public QWidget {

public:
    View(QWidget* parent, Data* data);

public:
    Data* getData() const;
    void setData(Data* data);
    void clear();
    void display() const;
    void update();

private:    
    void addWidget(QWidget* widget);
    void makeHeader() const;

private:
    Data* myData;
    QVBoxLayout* myBooksLayout;
    //QVBoxLayout* myHeaderLayout;
};

inline Data* View::getData() const {
    return myData;
}

#endif //_VIEW_H_
