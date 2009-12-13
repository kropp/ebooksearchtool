#ifndef _VIEW_H_
#define _VIEW_H_

#include <QWidget>

#include "../data/data.h"
#include "../view/bookwidget.h"
#include <QWidget>
#include <QGridLayout>

class QLabel;
class QCheckBox;
class QFile;

class Data;
class BookActionsButtonBox;


class View : public QWidget {

 Q_OBJECT

private:
    static QString ourConfigFilePath;
    
public:
    View(QWidget* parent, Data* data);

public:
    Data* getData() const;
    void setData(Data* data);
    void clear();
    void update();
    QSize sizeHint() const;

private:    
    void readSettings();
    void addWidget(QWidget* widget);
    void connectWithButtons() const;
    void hideHeader();
    void showHeader();

private slots:
    void markAllBooks(int);
    void download(BookWidget*);
    void toLibrary(BookWidget*);
    void remove(BookWidget*);
    void read(int requestId);
    void read();
    void removeChecked();
    void downloadChecked();
    void toLibraryChecked();
    
    void downloadBook(BookWidget*, const QString& fileName = QString());    

private:
    Data* myData;
    QList<BookWidget*> myBooks;

    QGridLayout* myLayout;

    QCheckBox* myCheckBox;
    BookActionsButtonBox* myBookActionsButtonBox;

    QFile* myFile;
    QString myReader;
    int myRequestId;
};

inline Data* View::getData() const {
    return myData;
}

#endif //_VIEW_H_
