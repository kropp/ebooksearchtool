#ifndef _VIEW_H_
#define _VIEW_H_

#include "../model/model.h"

#include <QWidget>

#include <QTextBrowser>

class View : public QWidget {

	Q_OBJECT
	
public:
	View(QWidget* parent);
	
public:
	void setModel(const Model* model);
	
private:
	void drawModel() const;

private:
	const Model* myModel;
	QTextBrowser* myTextBrowser;
};

#endif //_VIEW_H_
