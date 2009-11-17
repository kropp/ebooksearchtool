#ifndef _BOOK_WIDGET_H_
#define _BOOK_WIDGET_H_

#include <QWidget>

class QLabel;
class Book;
class HttpConnection;
class QFile;
class QIcon;
class QPushButton;
class QHBoxLayout;
class QCheckBox;
class QButtonGroup;
class BookActionsButtonBox;

class BookWidget : public QWidget {

    Q_OBJECT

public:
    BookWidget(QWidget* parent, const Book* book);
    virtual ~BookWidget();

private:
    void downloadCover();

private slots:
//    void paint() const;
    void setCover();

private:
    const Book* myBook;
    BookActionsButtonBox* myButtonGroup;

    QCheckBox* myCheckBox; // потому что будет важно его состояние
    //HttpConnection* myHttpConnection;
    //QFile* myFile;
};
#endif //_BOOK_WIDGET_H_
