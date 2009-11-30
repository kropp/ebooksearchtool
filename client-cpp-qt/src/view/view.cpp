#include "view.h"
#include "bookwidget.h"
#include <QLabel>
#include <QCheckBox>
#include <QFile>
#include <QProcess>

#include <QDebug>

View::View(QWidget* parent, Data* data) : QWidget(parent), myData(data) { 
    myHeaderLayout = new QHBoxLayout();
    myBooksLayout = new QVBoxLayout();
    myMainLayout = new QVBoxLayout();
	myMainLayout->addLayout(myHeaderLayout);
	myMainLayout->addLayout(myBooksLayout);
	setLayout(myMainLayout);
}

void View::setData(Data* data) {
    if (!myData) {
       makeHeader();
	}
	myData = data;
}

void View::update() {
    clear();
    if (!myData) {
        return;
    }
    const size_t size = (myData->getSize() < 5) ? myData->getSize() : 5;
    for (size_t i = 0; i < size; ++i) {
        BookWidget* widget = new BookWidget(this, myData->getBook(i));
        myBooks.push_back(widget);
        myBooksLayout->addWidget(widget);
    }
    updateHeader();
    connectWithButtons();
}

void View::clear() {
// remove all widgets from book layout
    const size_t count = myBooksLayout->count();
		for (size_t i = 0; i < count; ++i) {
        myBooksLayout->removeItem(myBooksLayout->itemAt(0));
	}
	myBooks.clear();
}

void View::markAllBooks(int state) {
    const size_t size = myBooks.size();
	for (size_t i = 0; i < size; ++i) {
	    myBooks[i]->mark(state);	
	}
}

void View::updateHeader() {
    QString shown = myShownLabel->text();
    int index = shown.lastIndexOf(" ") + 1;
    shown.remove(index, shown.size() - index);
    shown.push_back(QString::number(myBooks.size()));
    myShownLabel->setText(shown);

    QString found = myFoundLabel->text();
    index = found.lastIndexOf(" ") + 1;
    found.remove(index, found.size() - index);
    found.push_back(QString::number(myData->getTotalEntries()));
    myFoundLabel->setText(found);
}

void View::makeHeader() {
    myBookActionsButtonBox = new BookActionsButtonBox(this);
    myCheckBox = new QCheckBox();
	QVBoxLayout* buttonLayout = new QVBoxLayout();
	buttonLayout->addWidget(myBookActionsButtonBox);
	buttonLayout->addWidget(myCheckBox);
	myHeaderLayout->addLayout(buttonLayout);
   
    connect(myCheckBox, SIGNAL(stateChanged(int)), this, SLOT(markAllBooks(int)));
    connect(myBookActionsButtonBox, SIGNAL(remove()), this, SLOT(removeChecked()));

    QString found("FOUND: 0");
    QString shown("SHOWN: 0");
    myFoundLabel = new QLabel(found);
    myShownLabel = new QLabel(shown);
    myHeaderLayout->addWidget(myFoundLabel);
    myHeaderLayout->addWidget(myShownLabel);
}

void View::connectWithButtons() const {
    size_t size = myBooks.size();
    for (size_t i = 0; i < size; ++i) {
        connect(myBooks[i], SIGNAL(remove(BookWidget*)), this, SLOT(remove(BookWidget*)));
        connect(myBooks[i], SIGNAL(read(BookWidget*)), this, SLOT(downloadBook(BookWidget*)));
   }
}

void View::remove(BookWidget* widget) {
    int index = myBooks.indexOf(widget);
    if ((index >= 0) && (index < myBooks.size())) {
        myBooks.removeAt(index);
    }
    widget->hide();
}

void View::toLibrary(BookWidget*) {

}

void View::read(int id) {
    if (myRequestId != id) {
        return;
    }
    myFile->close();
//    qDebug() << "want to read File " << myFile->fileName();
    QProcess* process = new QProcess(this);
    process->start(QString::fromStdString("evince"), QStringList(myFile->fileName()));
// открываю подходящей читалкой
}

void View::downloadBook(BookWidget* widget) {
    const Book& book = widget->getBook();
    QString link = QString::fromStdString(book.getLink());
    
    // если файл с таким именем уже существует, то надо читать его
    
    myFile = new QFile(link.right(link.size() - link.lastIndexOf('/') - 1));
  //  qDebug() << "view SLOT read book link " << link;
   // qDebug() << "fileName " << myFile->fileName();
    myFile->open(QIODevice::WriteOnly);
    NetworkManager* connection = NetworkManager::getInstance();
    connect(connection, SIGNAL(requestFinished(int, bool)), this, SLOT(read(int)));  
    myRequestId = connection->download(link, myFile);
}

void View::removeChecked() {
    for (int i = 0; i < myBooks.size(); ++i) {
        if (myBooks[i]->isMarked()) {
            remove(myBooks[i]);
            --i;
        }
    }
}
