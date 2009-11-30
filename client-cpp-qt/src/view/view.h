#ifndef _VIEW_H_
#define _VIEW_H_

#include <QWidget>

#include "../data/data.h"
#include "../view/bookwidget.h"
#include <QWidget>
#include <QVBoxLayout>

class QLabel;
class QCheckBox;
class QFile;

class Data;
class BookActionsButtonBox;


class View : public QWidget {

 Q_OBJECT

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
    void connectWithButtons() const;


private slots:
    void markAllBooks(int);
    void remove(BookWidget*);
    void toLibrary(BookWidget*);
    void read(int requestId);
    void read();
    void removeChecked();
    void downloadBook(BookWidget*);    

private:
    Data* myData;
    QList<BookWidget*> myBooks;

    QVBoxLayout* myBooksLayout;
    QHBoxLayout* myHeaderLayout;
    QVBoxLayout* myMainLayout;

    QLabel* myFoundLabel;
    QLabel* myShownLabel;
    QCheckBox* myCheckBox;
    BookActionsButtonBox* myBookActionsButtonBox;

    QFile* myFile;
    int myRequestId;
};

inline Data* View::getData() const {
    return myData;
}

#endif //_VIEW_H_
