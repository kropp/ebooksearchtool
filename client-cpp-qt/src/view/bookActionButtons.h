#ifndef _BOOK_ACTION_BUTTON_BOX_H_
#define _BOOK_ACTION_BUTTON_BOX_H_

#include <QGroupBox>
#include <QPushButton>
#include <QHBoxLayout>
#include <QWidget>

class BookActionsButtonBox : public QGroupBox {

private:
    BookActionsButtonBox(QWidget* parent);

    void setButtons(QHBoxLayout* layout) const;
    void applyButtonSettings(QPushButton* button) const;

friend class BookWidget;
};

#endif //_BOOK_ACTION_BUTTON_BOX_H_
