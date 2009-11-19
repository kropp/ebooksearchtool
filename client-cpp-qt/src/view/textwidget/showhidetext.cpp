#include <QLabel>
#include <QPushButton>
#include <QHBoxLayout>

#include "showhidetext.h"


ShowHideText::ShowHideText(const QString& line, const QString& text, QWidget* parent) :QLabel(parent) {
    myLine = line; 
    myText = text; 
    setText(myLine);
 //прилепить к текстам гиперссылки
   
//    connect(myButton, SIGNAL(pressed()), this, SLOT(changeText()));
// еще один сигнал - изменить надпись на кнопке
}

//void ShowHideText::changeText() {
 //   if (myLabel->text() == myLine) {
        //myLabel->setText(myText);
//    } else {
     //   myLabel->setText(myLine);
  //  }
//}

