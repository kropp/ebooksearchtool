#ifndef _MORE_LESS_TEXT_LABEL_H_
#define _MORE_LESS_TEXT_LABEL_H_

#include <QLabel>

// сначала отображает текст myLine в конце гиперссылка "...", при нажатии на нее,
// текст меняется на myText, вид ссылки изменяется.

class MoreLessTextLabel : public QLabel {
    
    Q_OBJECT
    
public:
    MoreLessTextLabel(const QString& line, const QString& text, QWidget* parent);
    
private slots:
    void changeText();
    
private:
    QString myLine;
    QString myText;
};

#endif //_MORE_LESS_TEXT_LABEL_H_
