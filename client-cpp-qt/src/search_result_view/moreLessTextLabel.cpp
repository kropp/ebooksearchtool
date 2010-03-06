#include <QLabel>
#include <QPushButton>
#include <QHBoxLayout>
#include <QDebug>

#include "moreLessTextLabel.h"

MoreLessTextLabel::MoreLessTextLabel(const QString& line, const QString& text, QWidget* parent) :QLabel(parent) {
    myLine = line; 
    myLine.append("<a href=\"link\"> ... </a>");
    myText = text; 
    myText.append("  <a href=\"link\"> | </a>");
    setTextFormat(Qt::RichText);
    setWordWrap(true);
    setText(myLine);

    connect(this, SIGNAL(linkActivated(const QString&)), this, SLOT(changeText())); 
}

void MoreLessTextLabel::changeText() {
    if (text() == myLine) {
        setText(myText);
    } else {
        setText(myLine);
    }
}

