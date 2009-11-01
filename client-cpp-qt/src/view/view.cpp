#include "view.h"
#include "bookwidget.h"

View::View (QWidget* parent, Data* data) : QListWidget(parent) {
    resetData(data);
}

void View::resetData(Data* data) {
    myData = data;
    if (myData) {
        const size_t dataSize = myData->getSize();
        for (size_t i = 0; i < dataSize; ++i) {
            BookWidget* widget = new BookWidget(this, myData->getBook(i));
            addItem(widget);
            std::cout << "item added to the view\n";
        }
    }
}


//View::~View {
    // удалить все BookWidget
//}
/*View::View(QWidget* parent, Data* Data) : QWidget(parent) {
    myOneBookMode = false;
    myReadingProcess = 0;
    myData = Data;
	myTextBrowser = new QTextBrowser(this);	
	myTextBrowser->setReadOnly(true);
	myTextBrowser->resize(1000, 700); //сделать автоматическое удобное задание размеров
	connect(myTextBrowser, SIGNAL(sourceChanged(const QUrl&)), this, SLOT(downloadFile(const QUrl&)));
}

View::~View() {
	if (!myProcess) {
		myProcess.close();
		delete myProcess;
	}
	if (myData) {
	    delete myData;
	}
}
	
void View::resetData(Data* Data) {
    if (myData) {
        delete myData;
    }
    myData = Data;
  	drawData();
}


void View::update() const {
    drawData();
}

void View::drawData() const {
	myOneBookMode = (myData->getSize() > 1) ? false : true;
	myTextBrowser->clear();
	if (!myOneBookMode) {
	    QString status(" ");
	    makeStatusString(status);
	    myTextBrowser->insertPlainText(status);
	}
	const std::vector<const Book*> books = myData->getBooks();
	for (size_t i = 0; i < books.size(); ++i) {
		myTextBrowser->insertHtml(bookToHtml(books[i]));
	}
	
}

void View::makeStatusString(QString& str) const {
    str.setNum(myData->getTotalEntries());
    str.prepend("TOTAL RESULTS: ");
    str.append("   SHOWN: ");
    str.append(QString::number(myData->getSize()));
    str.append("\n");
    str.append("\n");
}

void View::downloadFile(const QUrl& url) {
	QString str = url.toString();	
	if (!str.contains(QString("READ"))) {
		str.append(".atom");
		myOneBookMode = true;
		emit urlRequest(str);
	} else {
		myOneBookMode = false;	
		std::vector<const Book*> books = myData->getBooks();
		emit urlRequest(books[0]->getLink().c_str());
	}
}

void View::open(const QString& fileName) {
	if (!myReadingProcess) {
		myReadingProcess = new QProcess(this);
	}
	myReadingProcess->close();
	myReadingProcess->start(QString("FBReader"), QStringList(fileName));
}


QString View::bookToHtml(const Book* book) const {
	QString html;
	if (myOneBookMode) {
		appendHeader(html, book->getTitle().c_str());
		//appendReference();
	} else {
		appendReference(html, book->getUri().c_str(), book->getTitle().c_str());
	}
	appendParagraph(html, book->getAuthor()->getName().c_str());
	appendParagraph(html, "Summary: ");	
	appendParagraph(html, book->getSummary().c_str());
	//appendParagraph(html, "author's uri: ");
	//appendParagraph(html, book->getAuthor()->getUri().c_str());
	appendParagraph(html, " ");
	if (myOneBookMode) {
		appendReference(html, "READ", "LOAD AND READ");
	}	
	return html;
}

void View::appendParagraph(QString& html, const QString& paragraph) const {
	html.append("<p>");
	html.append(paragraph);
	html.append("</p>");
}

void View::appendHeader(QString& html, const QString& header) const {
	html.append("<h3>");
	html.append(header);
	html.append("</h3>");	
}

void View::appendReference(QString& html, const QString& reference, const QString& text) const {
	html.append("<a href=\""); //<a href="subfolder/page2.htm">Щёлкните здесь для перехода на page 2</a>
	html.append(reference);
	html.append("\">");
	html.append(text);
	html.append("</a>");
}*/
