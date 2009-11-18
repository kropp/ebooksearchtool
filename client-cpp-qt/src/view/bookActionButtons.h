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

signals:
    void remove();
    void toLibrary();
    void read();
    
private:
    QPushButton* myDeleteButton;
    QPushButton* myToLibraryButton;
    QPushButton* myReadButton;

friend class BookWidget;
friend class View;
};

#endif //_BOOK_ACTION_BUTTON_BOX_H_
