#ifndef _VIEW_H_
#define _VIEW_H_

#include <QWidget>
#include <QGridLayout>

#include "../data/data.h"
#include "../view/bookwidget.h"


class QLabel;
class QCheckBox;
class QFile;
class Data;
class BookActionsButtonBox;

class View : public QWidget {

 Q_OBJECT

public:
    static const QString ourConfigFilePath;
    
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
    void connectToButtons() const;
    void hideHeader();
    void showHeader();
    void downloadToPath(const BookWidget* widget, const QString& path);
    QString getState() const;
    void authorsToString(const QVector<const Author*> authors, QString& names);

signals:
    void addToLibrary(const Book&);
    //void downloaded(const QString& bookTitle);
    void stateChanged(const QString& state);

private slots:
    void markAllBooks(int); 
    void remove(BookWidget*); 

    void download(BookWidget*);
    void bookDownloaded(int id);
    void toLibrary(BookWidget*);
    void read(BookWidget*);
    void read(int requestId);
    void open(const QString& fileName) const;

    void removeChecked();
    void downloadChecked();
    void toLibraryChecked();
    

private:
    Data* myData;
    
    QList<BookWidget*> myBooks;

    QGridLayout* myLayout;

    QCheckBox* myCheckBox;
    BookActionsButtonBox* myBookActionsButtonBox;

    QFile* myFile;
    QString myReader;
    int myRequestId;
    const BookWidget* myActiveWidget;
    bool myWantToRead;
};

inline Data* View::getData() const {
    return myData;
}

#endif //_VIEW_H_
