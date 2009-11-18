#ifndef _VIEW_H_
#define _VIEW_H_

#include <QWidget>

#include "../data/data.h"
#include "../view/bookwidget.h"
#include <QWidget>
#include <QVBoxLayout>

class Data;
class QLabel;

class View : public QWidget {

public:
    View(QWidget* parent, Data* data);

public:
    Data* getData() const;
    void setData(Data* data);
    void clear();
    void update();

private:    
    void addWidget(QWidget* widget);
    void makeHeader();
    void updateHeader();

private:
    Data* myData;
    QVBoxLayout* myBooksLayout;
    QHBoxLayout* myHeaderLayout;
    QVBoxLayout* myMainLayout;
		QLabel* myFoundLabel;
		QLabel* myShownLabel;
    size_t myBooksNumber;
};

inline Data* View::getData() const {
    return myData;
}

#endif //_VIEW_H_
