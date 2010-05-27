#include "filedownloader.h"
#include "filedownloadmanager.h"
#include "settingsmanager.h"
#include "book.h"

#include <QDebug>
#include <QUrl>
#include <QDesktopServices>
#include <QApplication>

FileDownloadManager* FileDownloadManager::instance = 0;


static const QString COVER_DIR = "./";

static const int IMAGE_DOWNLOAD_URL_PORT = 80;

void FileDownloadManager::openLocalFile(QString filename) {
//  need absolute file path

//    QString appPath = QApplication::applicationDirPath();
//    QDesktopServices::openUrl(QUrl("file:///" + appPath + "/" + filename));

        QDesktopServices::openUrl(QUrl("file:///" + filename));

}


FileDownloadManager::FileDownloadManager() {
    qDebug() << "FileDownloadManager::FileDownloadManager";

    myConnectionForCovers = new QHttp(this);
    myConnectionForCovers->setHost("www.feedbooks.com", 80);

    initializeDownloaders();
    setConnections();
}

FileDownloadManager* FileDownloadManager::getInstance() {
    if (!instance) {
        instance = new FileDownloadManager();
    }
    return instance;
}

QString FileDownloadManager::getCoverDir()
{
    return COVER_DIR;
}

int FileDownloadManager::downloadCover(QString urlStr, QIODevice* out) {
    QUrl url(urlStr);
//
//    QString urlHost = url.host();
    QString urlPath = tr("/book/547.jpg?t=20090916102557");

//    myConnectionForCovers->setHost(urlHost, IMAGE_DOWNLOAD_URL_PORT);

    QString proxy = SettingsManager::getInstance()->getProxy();

    if (proxy.size() != 0)
    {
       myConnectionForCovers->setProxy(proxy, SettingsManager::getInstance()->getProxyPort());
    }

   // qDebug() << "NetworkManager::downloadCover request =" << url.host()<<  url.path();
    int id = myConnectionForCovers->get(urlPath, out);

    return id;
}

void FileDownloadManager::coverDownloadRequestFinished(int request, bool success)
{
    emit coverRequestFinished(request, success);
}

int FileDownloadManager::downloadBook(const Book& book, const QString& filename, const QString& format) {
    return myDownloader->startDownloadingFile(book.getSourceLinks().value(format), filename, false);
}

int FileDownloadManager::readBook(const Book& book, const QString& filename, const QString& format) {

    return myDownloader->startDownloadingFile(book.getSourceLinks().value(format), filename, true);
}

void FileDownloadManager::initializeDownloaders() {
    myDownloader = new FileDownloader("www.feedbooks.com", false);

}

void FileDownloadManager::abortDownloaders() {

}

QString FileDownloadManager::getReadDefaultLocation()
{
    return "tempreadfile." + SettingsManager::getInstance()->getCurrentFormat();
}

void FileDownloadManager::setConnections() {
    connect(myConnectionForCovers, SIGNAL(requestFinished(int,bool)), this, SLOT(coverDownloadRequestFinished(int,bool)));
    connect(myDownloader, SIGNAL(downloadFinished(bool, QString, int)), this, SLOT(downloadFinished(bool, QString, int)));
}

void FileDownloadManager::downloadFinished(bool success, QString filename, int request) {

    emit downloadBookFinished(success, request);

    if (success) {
        qDebug() << "FileDownloadManager::downloadFinished SUCCESS" << filename;
    } else {
        qDebug() << "FileDownloadManager::downloadFinished ERROR" << filename;
    }
}

