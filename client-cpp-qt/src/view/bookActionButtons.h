#ifndef _BOOK_ACTION_BUTTON_BOX_H_
#define _BOOK_ACTION_BUTTON_BOX_H_

#include <QGroupBox>
#include <QPushButton>
#include <QHBoxLayout>
#include <QWidget>

class QPushButton;

class BookActionsButtonBox : public QGroupBox {

    Q_OBJECT

private:
    BookActionsButtonBox(QWidget* parent);

    void createButtons(QHBoxLayout* layout);
    void applyButtonSettings(QPushButton* button) const;

public:
    QSize sizeHint() const;

signals:
    void toLibrary();
    void download();
    void read();
    void remove();
    void getInfo();
    

private:
    QPushButton* myToLibraryButton;
    QPushButton* myDownloadButton;
    QPushButton* myReadButton;
    QPushButton* myRemoveButton;
    QPushButton* myInfoButton;

friend class BookWidget;
friend class View;
};

#endif //_BOOK_ACTION_BUTTON_BOX_H_
