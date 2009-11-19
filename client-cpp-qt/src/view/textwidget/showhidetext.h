#ifndef _SHOW_HIDE_TEXT_H_
#define _SHOW_HIDE_TEXT_H_

#include <QLabel>

// сначала отображает текст myLine в конце гиперссылка "...", при нажатии на нее,
// текст меняется на myText, вид ссылки изменяется.

class ShowHideText : public QLabel {
    
public:
    ShowHideText(const QString& line, const QString& text, QWidget* parent);
    
private:
    QString myLine;
    QString myText;
};

#endif //_SHOW_HIDE_TEXT_H_
