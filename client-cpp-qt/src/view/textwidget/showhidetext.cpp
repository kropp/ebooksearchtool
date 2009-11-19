#include <QLabel>
#include <QPushButton>
#include <QHBoxLayout>

#include "showhidetext.h"


ShowHideText::ShowHideText(const QString& line, const QString& text, QWidget* parent) : QWidget(parent), myLine(line), myText(text) {
    myButton = new QPushButton(tr("show"));
    myLabel = new QLabel(myLine);
    QHBoxLayout* mainLayout = new QHBoxLayout();
    mainLayout->addWidget(myLabel);
    mainLayout->addWidget(myButton);
    
    connect(myButton, SIGNAL(pressed()), this, SLOT(changeText()));
// еще один сигнал - изменить надпись на кнопке
    setLayout(mainLayout);
}

void ShowHideText::changeText() {
    if (myLabel->text() == myLine) {
        //myLabel->setText(myText);
//    } else {
     //   myLabel->setText(myLine);
  //  }
}

