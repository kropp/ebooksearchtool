#include "view.h"
#include "bookwidget.h"
#include <QLabel>
#include <QCheckBox>
//#include <QFile>
//#include <QProcess>
#include <QSettings>
//#include <QFileDialog>

#include <QDebug>

const QString View::ourConfigFilePath = "../.config.ini";

View::View(QWidget* parent, Data* data) : QWidget(parent), myData(data), myWantToRead(false) { 
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
    myLayout->addWidget(myCheckBox, 0, 3, Qt::AlignCenter);
    myLayout->addWidget(myBookActionsButtonBox, 0, 2, Qt::AlignRight);

//format layout   
    myLayout->setColumnStretch(0, 1);
    myLayout->setColumnStretch(1, 13);
    myLayout->setColumnStretch(2, 2);
    myLayout->setColumnStretch(3, 1);

	setLayout(myLayout);
}

void View::setData(Data* data) {
    myData = data;
//    if (myData->getSize() != 0) {
   /*     QString state(tr("Found: "));
        state.append(myData->getTotalEntries());
        state.append("  Shown: "); 
        state.append(myData->getSize());
        emit stateChanged(state);
 */
 //  }
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
    emit stateChanged(getState());
    connectToButtons();
}

QString View::getState() const {
    QString state(tr("Found: "));
    QString number;
    number.setNum(myData->getTotalEntries());
    state.append(number);
    state.append("  Shown: ");
    number.setNum(myBooks.size());
    state.append(number);
    return state;
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

QSize View::sizeHint() const {
    if (myBooks.empty()) {
        return QSize();
    }
    const QSize size = myBooks[0]->sizeHint();
    return QSize(size.width(), size.height()*(myBooks.size() + 1));
}

void View::readSettings() {
    QSettings settings(ourConfigFilePath, QSettings::IniFormat);
    myReader = settings.value("view/reader").toString();
}

void View::hideHeader() {
    myCheckBox->hide();
    myBookActionsButtonBox->hide();
}

void View::showHeader() {
    myCheckBox->show();
    myBookActionsButtonBox->show();
}


void View::connectToButtons() const {
    size_t size = myBooks.size();
    for (size_t i = 0; i < size; ++i) {
        connect(myBooks[i], SIGNAL(download(BookWidget*)), this, SLOT(download(BookWidget*)));
        connect(myBooks[i], SIGNAL(read(BookWidget*)), this, SLOT(read(BookWidget*)));
        connect(myBooks[i], SIGNAL(toLibrary(BookWidget*)), this, SLOT(toLibrary(BookWidget*)));
        connect(myBooks[i], SIGNAL(remove(BookWidget*)), this, SLOT(remove(BookWidget*)));
   }
}

