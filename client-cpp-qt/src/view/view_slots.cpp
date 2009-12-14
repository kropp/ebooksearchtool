#include "view.h"
#include "bookwidget.h"
#include <QFile>
#include <QProcess>
#include <QFileDialog>

#include <QDebug>

void View::markAllBooks(int state) {
    const size_t size = myBooks.size();
	for (size_t i = 0; i < size; ++i) {
	    myBooks[i]->mark(state);	
	}
}

void View::remove(BookWidget* widget) {
    int index = myBooks.indexOf(widget);
    if ((index >= 0) && (index < myBooks.size())) {
        myBooks.removeAt(index);
    }
    widget->hide();
}

void View::toLibrary(BookWidget* widget) {
    const Book& book = widget->getBook();
    qDebug() << "view signal book to library " << QString::fromStdString(book.getTitle());
    emit addToLibrary(book);
}

void View::read(BookWidget* ) {
   qDebug() <<  "slot View::read";
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
    qDebug() << "View::read " << myReader << "  " << myFile->fileName();
    process->start(myReader, QStringList(myFile->fileName()));
}

void View::download(BookWidget* widget) {
     qDebug() << "View slot download book";
     // указывать формат, считанный из настроек
     QString fileName(QFileDialog::getSaveFileName(this, tr("Download book"), tr(""), tr("*.pdf")));
     qDebug() << "file name for saving " << fileName;
     if (!fileName.isEmpty()) {
        downloadToPath(widget, fileName);     
     }
}

void View::downloadToPath(const BookWidget* widget, const QString& name) {
    const Book& book = widget->getBook();
    QString link = QString::fromStdString(book.getLink());
    QString fileName;
    if (!name.isEmpty()) {
        fileName = name;
    } else {
        fileName = link.right(link.size() - link.lastIndexOf('/') - 1);
    }
    myFile = new QFile(fileName);
    myFile->open(QIODevice::WriteOnly);
    NetworkManager* connection = NetworkManager::getInstance();
   
    connect(connection, SIGNAL(requestFinished(int, bool)), this, SLOT(bookDownloaded(int)));  
    myActiveWidget = widget;
    myRequestId = connection->download(link, myFile);
}

void View::bookDownloaded(int id) {
    if (id == myRequestId) {
        const Book& book = myActiveWidget->getBook();
        qDebug() << "signal View::BookDownloaded" << QString::fromStdString(book.getTitle());
        QString title = QString::fromStdString(book.getTitle());
        emit stateChanged(title.prepend(tr("Downloaded book: ")));
    }
}

void View::removeChecked() {
    for (int i = 0; i < myBooks.size(); ++i) {
        if (myBooks[i]->isMarked()) {
            remove(myBooks[i]);
            --i;
        }
    }
}
    
void View::downloadChecked() {
    qDebug() << "View slot downloadChecked";
    // один раз показываем файловое диалоговое окно
    // вызваем download слот 
    // для каждого из помеченных
}

void View::toLibraryChecked() {
    qDebug() << "View slot toLibraryChecked";
    for (int i = 0; i < myBooks.size(); ++i) {
        if (myBooks[i]->isMarked()) {
            toLibrary(myBooks[i]);
            --i;
        }
    }
}
