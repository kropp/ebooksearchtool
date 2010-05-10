#ifndef MULTISTATEBUTTON_H
#define MULTISTATEBUTTON_H

#include <QPushButton>

class MultiStateButton : public QPushButton
{

Q_OBJECT

public:

    MultiStateButton(QWidget *parent = 0);

public:

     Q_PROPERTY(QString state READ getState WRITE setState);

public:

     QString getState();
     void setState(QString value);

private:

     QString state;

};

#endif // MULTISTATEBUTTON_H
