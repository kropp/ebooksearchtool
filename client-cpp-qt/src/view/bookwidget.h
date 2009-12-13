#ifndef _BOOK_WIDGET_H_
#define _BOOK_WIDGET_H_

#include <QWidget>
#include <QCheckBox>

#include "bookActionButtons.h"
#include "../network/networkmanager.h"
#include "moreLessTextLabel.h"

class QLabel;
class Book;
class QFile;
class QImage;
class QPushButton;
class QHBoxLayout;

class BookWidget : public QWidget {

    Q_OBJECT

private:
    static const QSize ourSizeHint;

public:
    BookWidget(QWidget* parent, const Book* book);

    const Book& getBook() const;
//    bool isMarked() const;

//    void mark(int state);
    QSize sizeHint() const;

signals:
    void toLibrary(BookWidget*);
    void download(BookWidget*);
    void read(BookWidget*);

private slots:
    void setCover(int requestId);
    void toLibrary();
    void download();
    void read();

private: 
    void downloadCover();
    void setCover();
    QLabel* makeSummary();
    void setBackground();
    
private:
    const Book* myBook;
    
    QGridLayout* myMainLayout;
  //  QCheckBox* myCheckBox;
    QFile* myCoverFile;    
    
    int myRequestId;
};

inline const Book& BookWidget::getBook() const {
	return *myBook;
}

/*inline bool BookWidget::isMarked() const {
    return myCheckBox->checkState();
}
*/

#endif //_BOOK_WIDGET_H_
