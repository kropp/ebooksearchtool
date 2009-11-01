#ifndef _BOOK_WIDGET_H_
#define _BOOK_WIDGET_H_

class QLabel;
class Book;

class BookWidget : public QWidget {
public:
    BookWidget(QWidget* parent, const Book* book);

public:
    void paint() const;

private:
    const Book* myBook;
    QLabel* myTitle;
};
#endif //_BOOK_WIDGET_H_
