#ifndef _bookDescriptionDialog_h_
#define _bookDescriptionDialog_h_

#include <QDialog>
#include "../data/book_author.h"

class BookDescriptionDialog : public QDialog {
public:
    BookDescriptionDialog(QWidget* parent, const Book& book);
    
    ~BookDescriptionDialog() {}

private:
    void setText(QTextEdit* text, const Book& book);
};


#endif // _bookDescriptionDialog_h_
