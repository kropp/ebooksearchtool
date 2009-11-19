#ifndef _SHOW_HIDE_TEXT_H_
#define _SHOW_HIDE_TEXT_H_

#include <QWidget>

class QLabel;
class QPushButton;

class ShowHideText : public QWidget {
    
    Q_OBJECT
    
public:
    ShowHideText(const QString& text1, const QString& text2, QWidget* parent);
    
private slots:
    void changeText();
    
private:
    const QString& myLine;
    const QString& myText;
    QPushButton* myButton;
    QLabel* myLabel;
};

#endif //_SHOW_HIDE_TEXT_H_
