#include "view.h"
#include "bookwidget.h"
#include <QLabel>
#include <QCheckBox>
#include <QFile>
#include <QProcess>

#include <QDebug>

View::View(QWidget* parent, Data* data) : QWidget(parent), myData(data) { 
    myBooksLayout = new QVBoxLayout();
	setLayout(myBooksLayout);
}

void View::setData(Data* data) {
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
    read();
}

void View::read() {
    QProcess* process = new QProcess(); //(this); и обрабатывать сигнал об уничтожении родительского процесса
    process->start(QString::fromStdString("evince"), QStringList(myFile->fileName()));
    
}

void View::downloadBook(BookWidget* widget) {
    const Book& book = widget->getBook();
    QString link = QString::fromStdString(book.getLink());
    QString fileName = link.right(link.size() - link.lastIndexOf('/') - 1);
    
    // если файл с таким именем уже существует, то надо читать его
    if (QFile::exists(fileName)) {
        myFile = new QFile(fileName);
        read();
        return;
    }
    
    myFile = new QFile(fileName);
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
