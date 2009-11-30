#ifndef _BOOK_WIDGET_H_
#define _BOOK_WIDGET_H_

#include <QWidget>
#include <QCheckBox>

#include "bookActionButtons.h"
#include "../network/networkmanager.h"
#include "moreLessTextLabel.h"

class QLabel;
class Book;
class HttpConnection;
class QFile;
class QImage;
class QPushButton;
class QHBoxLayout;

class BookWidget : public QWidget {

    Q_OBJECT

public:
    BookWidget(QWidget* parent, const Book* book);

    const Book& getBook() const;
    bool isMarked() const;

    void mark(int state);

signals:
    void remove(BookWidget*);
    void toLibrary(BookWidget*);
    void read(BookWidget*);

private slots:
    void setCover(int requestId);
    void remove();
    void toLibrary();
    void read();

private: 
    void downloadCover();
    void setCover();
    MoreLessTextLabel* makeSummary();
    void setBackground();
    
private:
    const Book* myBook;
    
    QGridLayout* myMainLayout;
    QCheckBox* myCheckBox;
    QFile* myCoverFile;    
    
    int myRequestId;
};

inline const Book& BookWidget::getBook() const {
	return *myBook;
}

inline bool BookWidget::isMarked() const {
    return myCheckBox->checkState();
}

#endif //_BOOK_WIDGET_H_
