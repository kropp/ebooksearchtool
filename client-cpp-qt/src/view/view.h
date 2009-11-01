#ifndef _VIEW_H_
#define _VIEW_H_

#include "../data/data.h"

#include <QListWidget>
#include <../network/httpconnection.h>

class Data; 

class View : public QListWidget {

public:
    View(QWidget* parent, Data* data);

public:
    Data* getData() const;
    void resetData(Data* data);

private:
    Data* myData;
};

inline Data* View::getData() const {
    return myData;
}

/*class View : public QWidget {

	Q_OBJECT
	
public:
	View(QWidget* parent, Data* Data);
	~View();
	
public:
	void resetData(Data* Data);
	void open(const QString& fileName);
	void update() const;
	Data* getData();
	
private slots:
	void downloadFile(const QUrl&);
	
signals:
	void urlRequest(const QString&);

private:
	void drawData() const;
	void makeStatusString(QString& str) const;
	QString bookToHtml(const Book*) const;
	void appendParagraph(QString& html, const QString& paragraph) const;
	void appendHeader(QString& html, const QString& header) const;
	void appendReference(QString& html, const QString& reference, const QString& text) const;

private:
	Data* myData; 
	QTextBrowser* myTextBrowser;

	mutable bool myOneBookMode;

	QProcess* myReadingProcess;
};


inline Data* View::getData() {
    return myData;
}*/


#endif //_VIEW_H_
