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
	View(QWidget* parent);
	
public:
	void setModel(const Model* model);
	
private slots:
	void downloadFile(const QUrl&);
	
signals:
	void urlRequest(const QString&);

private:
	void drawModel();
	QString bookToHtml(const Book*) const;
	void appendParagraph(QString& html, const QString& paragraph) const;
	void appendHeader(QString& html, const QString& header) const;
	void appendReference(QString& html, const QString& reference, const QString& text) const;

private:
	const Model* myModel;
	QTextBrowser* myTextBrowser;

	bool myOneBookMode;

	QProcess* myReadingProcess;
};

#endif //_VIEW_H_
