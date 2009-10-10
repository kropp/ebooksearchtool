#ifndef _VIEW_H_
#define _VIEW_H_

#include "../model/model.h"

#include <QWidget>

class QTextEdit;

class View : public QWidget {
	
public:
	View(QWidget* parent);
	
public:
	void setModel(const Model* model);
	
private:
	void drawModel() const;

private:
	const Model* myModel;
	QTextEdit* myText;
};

#endif //_VIEW_H_
