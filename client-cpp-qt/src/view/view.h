#ifndef _VIEW_H_
#define _VIEW_H_

#include "../model/model.h"

#include <QWidget>
#include <QTextBrowser>
#include <QProcess>

#include <../network/httpconnection.h>

class View : public QWidget {

	Q_OBJECT
	
public:
	View(QWidget* parent, Model* model);
	~View();
	
public:
	void resetModel(Model* model);
	void open(const QString& fileName);
	void update() const;
	Model* getModel();
	
private slots:
	void downloadFile(const QUrl&);
	
signals:
	void urlRequest(const QString&);

private:
	void drawModel() const;
	void makeStatusString(QString& str) const;
	QString bookToHtml(const Book*) const;
	void appendParagraph(QString& html, const QString& paragraph) const;
	void appendHeader(QString& html, const QString& header) const;
	void appendReference(QString& html, const QString& reference, const QString& text) const;

private:
	Model* myModel; 
	QTextBrowser* myTextBrowser;

	mutable bool myOneBookMode;

	QProcess* myReadingProcess;
};


inline Model* View::getModel() {
    return myModel;
}


#endif //_VIEW_H_
