#include "multistatebutton.h"

static const QString NORMAL_STATE = "normal";

MultiStateButton::MultiStateButton(QWidget *parent) :
    QPushButton(parent)
{
    state = NORMAL_STATE;
    setCursor(Qt::PointingHandCursor);
}

QString MultiStateButton::getState()
{
    return state;
}

void MultiStateButton::setState(QString value)
{
    state = value;
}
