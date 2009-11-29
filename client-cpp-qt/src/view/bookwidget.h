#ifndef _BOOK_WIDGET_H_
#define _BOOK_WIDGET_H_

#include <QWidget>
#include "bookActionButtons.h"
#include "../network/networkmanager.h"

class QLabel;
class Book;
class HttpConnection;
class QFile;
class QImage;
class QPushButton;
class QHBoxLayout;
class QCheckBox;

class BookWidget : public QWidget {

    Q_OBJECT

public:
    BookWidget(QWidget* parent, const Book* book);

    const Book& getBook() const;
    bool isMarked() const;
    
    void mark(int state);

private slots:
    void setCover(int requestId);
    void remove();
    void toLibrary();
    void read();

signals:
    void remove(BookWidget*);
    void toLibrary(BookWidget*);
    void read(BookWidget*);
    
private:
    const Book* myBook;
    
    QCheckBox* myCheckBox;
    QLabel* myCover;
    BookActionsButtonBox* myButtonGroup;
    
    NetworkManager *myConnection;
    int myRequestId;
    QDataStream* myDataStream;
    QFile* myFile;    
};

#endif //_BOOK_WIDGET_H_
