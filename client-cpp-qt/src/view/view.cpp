#include "view.h"
#include "bookwidget.h"
#include <QLabel>
#include <QCheckBox>
#include <QFile>
#include <QProcess>
#include <QSettings>
#include <QFileDialog>

#include <QDebug>

QString View::ourConfigFilePath = "../.config.ini";

View::View(QWidget* parent, Data* data) : QWidget(parent), myData(data) { 
    readSettings();
    myLayout = new QGridLayout(this);

//make header    
    myCheckBox = new QCheckBox(this);
    connect(myCheckBox, SIGNAL(stateChanged(int)), this, SLOT(markAllBooks(int)));

    myBookActionsButtonBox = new BookActionsButtonBox(this);
    connect(myBookActionsButtonBox, SIGNAL(toLibrary()), this, SLOT(toLibraryChecked()));
    connect(myBookActionsButtonBox, SIGNAL(download()), this, SLOT(downloadChecked()));
    connect(myBookActionsButtonBox, SIGNAL(remove()), this, SLOT(removeChecked()));

    hideHeader();
    myLayout->addWidget(myCheckBox, 0, 3, Qt::AlignRight);
    myLayout->addWidget(myBookActionsButtonBox, 0, 2, Qt::AlignLeft);

//format layout   
    myLayout->setColumnStretch(0, 1);
    myLayout->setColumnStretch(1, 4);
    myLayout->setColumnStretch(2, 2);

	setLayout(myLayout);
}

void View::setData(Data* data) {
    myData = data;
}
    
QSize View::sizeHint() const {
    if (myBooks.empty()) {
        return QSize();
    }
    const QSize size = myBooks[0]->sizeHint();
    return QSize(size.width(), size.height()*(myBooks.size() + 1));
}

void View::update() {
    clear();
    if (!myData) {
        hideHeader();
        return;
    }
    const size_t size = myData->getSize();

//don't show header if there are no books
    if (size == 0) {
        hideHeader();
    } else {
        showHeader();
    }
//show books
    for (size_t i = 0; i < size; ++i) {
        BookWidget* widget = new BookWidget(this, myData->getBook(i));
        myBooks.push_back(widget);
        myLayout->addWidget(widget, i + 1, 0, 1, 4);
        qDebug() << "View::update widget added";
    }
    connectToButtons();
}

void View::clear() {
// remove all widgets from book layout
// ??it may be done faster. method for clearing layout.
   /* const size_t count = myBooksLayout->count();
		for (size_t i = count; i > 0; --i) {
        myBooksLayout->removeItem(myBooksLayout->itemAt(0));
	    BookWidget* widget = myBooks.back();
        myBooks.pop_back();
        delete widget;
    }
    */
   for (int i = 0; i < myBooks.size(); ++i) {
       myBooks[i]->hide(); 
   }
   myBooks.clear();
}

void View::markAllBooks(int state) {
    const size_t size = myBooks.size();
	for (size_t i = 0; i < size; ++i) {
	    myBooks[i]->mark(state);	
	}
}


void View::connectToButtons() const {
    size_t size = myBooks.size();
    for (size_t i = 0; i < size; ++i) {
        connect(myBooks[i], SIGNAL(download(BookWidget*)), this, SLOT(download(BookWidget*)));
        connect(myBooks[i], SIGNAL(read(BookWidget*)), this, SLOT(downloadBook(BookWidget*)));
        connect(myBooks[i], SIGNAL(toLibrary(BookWidget*)), this, SLOT(toLibrary(BookWidget*)));
        connect(myBooks[i], SIGNAL(remove(BookWidget*)), this, SLOT(remove(BookWidget*)));
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
     QString fileName(QFileDialog::getSaveFileName(this, tr("Download book"), tr(""), tr("Checkers Files (*.pdf)")));
     // указывать формат, считанный из настроек
     if (!fileName.isEmpty()) {
        downloadBook(widget, fileName);     
     }
}

void View::readSettings() {
    QSettings settings(ourConfigFilePath, QSettings::IniFormat);
    myReader = settings.value("view/reader").toString();
}

void View::downloadBook(BookWidget* widget, const QString& name) {
    const Book& book = widget->getBook();
    QString link = QString::fromStdString(book.getLink());
    QString fileName;
    if (!name.isEmpty()) {
        fileName = name;
    } else {
        fileName = link.right(link.size() - link.lastIndexOf('/') - 1);
    }
   // qDebug() <<
    // если файл с таким именем уже существует, то надо читать его
   /* if (QFile::exists(fileName)) {
        myFile = new QFile(fileName);
        read();
        return;
    }
    */
    myFile = new QFile(fileName);
    myFile->open(QIODevice::WriteOnly);
    NetworkManager* connection = NetworkManager::getInstance();
   
   //change to slot book downloaded
   // connect(connection, SIGNAL(requestFinished(int, bool)), this, SLOT(read(int)));  

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

void View::hideHeader() {
    myCheckBox->hide();
    myBookActionsButtonBox->hide();
}

void View::showHeader() {
    myCheckBox->show();
    myBookActionsButtonBox->show();
}
