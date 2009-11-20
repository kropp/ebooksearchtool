#ifndef _BOOK_WIDGET_H_
#define _BOOK_WIDGET_H_

#include <QWidget>
#include "bookActionButtons.h"

class QLabel;
class Book;
class HttpConnection;
class QFile;
class QIcon;
class QPushButton;
class QHBoxLayout;
class QCheckBox;

class BookWidget : public QWidget {

    Q_OBJECT

public:
    BookWidget(QWidget* parent, const Book* book);
    virtual ~BookWidget();

    const Book& getBook() const;
    bool isMarked() const;
    
    void mark(int state);

private:
    void downloadCover();

private slots:
    void setCover();
    void remove();
    void toLibrary();
    void read();

signals:
    void remove(BookWidget*);
    void toLibrary(BookWidget*);
    void read(BookWidget*);
    
private:
    const Book* myBook;
    BookActionsButtonBox* myButtonGroup;

    QCheckBox* myCheckBox; // потому что будет важно его состояние
    
		//QFile* myFile;
};
#endif //_BOOK_WIDGET_H_
