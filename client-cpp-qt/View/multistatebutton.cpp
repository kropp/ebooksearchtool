#include "multistatebutton.h"

static const QString NORMAL_STATE = "normal";

MultiStateButton::MultiStateButton(QWidget *parent) :
    QPushButton(parent)
{
    myState = NORMAL_STATE;
}

QString MultiStateButton::getState()
{
    return myState;
}

void MultiStateButton::setState(QString value)
{
    myState = value;
}
