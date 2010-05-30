#include "book.h"
#include "filedownloadmanager.h"

#include <QDebug>
#include <QFileInfo>

static const int HTTP_PREFIX_LENGTH = 7;

Book::Book(const QString& title,
           const QString& language,
           const QString& summary,
           const QString& id) : myTitle (title),
myLanguage(language),
mySummary(summary),
myId(id) {}


Book::Book()
{
    myDownloadCoverId = -1;
    coverDownloadRequested = false;
}


bool Book::compareAuthors(const Book* book1, const Book* book2) {
    return book1->getAuthors()[0]->getName() < book2->getAuthors()[0]->getName();
}

bool Book::compareTitles(const Book* book1, const Book* book2) {
    return book1->getTitle() < book2->getTitle();
}

void Book::setServerName(const QString& server) {
    myServerName = server;
}

void Book::setTitle(const QString& title) {
    myTitle = title;
}

void Book::setLanguage(const QString& lang) {
    myLanguage = lang;
}

void Book::setSummary(const QString& summary) {
    mySummary = summary;
}

void Book::setId(const QString& id) {
    myId = id;
}

void Book::addAuthor(const Author* author) {
    myAuthors.push_back(author);
}

void Book::setCoverLink(const QString& path) {
    myCoverLink = path;
}

const QVector<const Author*>& Book::getAuthors() const {
    return myAuthors;
}

void Book::setContent(const QString& content) {
    myContent = content;
}

void Book::addSourceLink(const QString& format, const QString& link) {
    mySourceLinks->insert(format, link);
}

void Book::setSourceLinks(QMap<QString, QString>* links) {
    mySourceLinks = links;
}


//QString Book::getSourceLink() const {
//    qDebug() << " Book::getSourceLink default - return pdf-link !!!!";
//    return getSourceLink("pdf");
//}
//
//const QString Book::getSourceLink(const QString& format) const {
//    QMap<QString, QString>::const_iterator i = mySourceLinks.find(format);
//    if (i == mySourceLinks.constEnd()) {
//        return QString();
//    } else {
//        return i.value();
//    }
//}

const QMap<QString, QString>& Book::getSourceLinks() const {
    return *mySourceLinks;
}


void Book::setUpdated(const QString& updated) {
    myUpdated = updated;
}

void Book::setIssued(const QString& issued) {
    myIssued = issued;
}

void Book::setPublisher(const QString& publisher) {
    myPublisher = publisher;
}
void Book::setRights(const QString& rights) {
    myRights = rights;
}

void Book::addCategory(const QString& category) {
    myCategories.push_back(category);
}

void Book::downloadCover() {

    if (!coverDownloadRequested)
    {
        coverDownloadRequested = true;

        const QString& coverLink = getCoverLink();
        if (coverLink.isEmpty()) {
            return;
        }

        QString fileName = coverLink.right(coverLink.size() - coverLink.lastIndexOf('/') - 1);
        fileName = fileName.left(fileName.indexOf('?')); //FileDownloadManager::getInstance()->getCoverDir() +

        //if such file exists - just open it and return;
        myCoverFile = new QFile(fileName);

        if ((QFile::exists(fileName)) && (myCoverFile->bytesAvailable() != 0)) {
            setCover(-1, false);
            return;
        }
        if (!myCoverFile->open(QIODevice::WriteOnly))  {
            qDebug() << "Book::downloadCover()  ERROR open file " << fileName;
        }

        //QString coverLinkWithoutHttp = coverLink.right(coverLink.length() - HTTP_PREFIX_LENGTH);
        myDownloadCoverId = FileDownloadManager::getInstance()->downloadCover(coverLink, myCoverFile);
        connect(FileDownloadManager::getInstance(), SIGNAL(coverRequestFinished(int, bool)), this, SLOT(setCover(int, bool)));
    }
}


void Book::setCover(int requestId, bool success)
{
    if (success)
    {
        if (requestId == myDownloadCoverId)
        {
            QIcon* coverIcon = new QIcon(myCoverFile->fileName());
            myCoverFile->close();
            emit bookCoverChanged(coverIcon);
        }
    }  else if (requestId == -1)  {
        QIcon* coverIcon = new QIcon(myCoverFile->fileName());
        emit bookCoverChanged(coverIcon);

    } else {
        myCoverFile->remove();
    }

}

void Book::setLocalLink(QPair<QString, QString> link) {
    qDebug() << "Book::setLocalLink( ) " << myTitle << "  link =  " << link.second;
    myLocalLink.first = link.first;
    QFileInfo fileInfo(link.second);
    link.second = fileInfo.absoluteFilePath();
}


bool Book::hasLocalLink() {
    if (myLocalLink.first.isEmpty()) {
        return false;
    }
    if (QFile::exists(myLocalLink.second)) {
        return true;
    } else {
        myLocalLink.first = QString();
        myLocalLink.second = QString();
        return false;
    }
}


const QPair<QString, QString>&  Book::getLocalLink() const {
    //    hasLocalLink();
    return myLocalLink;
}
