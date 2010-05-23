#include "filedownloader.h"
#include "filedownloadmanager.h"
#include "settingsmanager.h"
#include "book.h"

#include <QDebug>

FileDownloadManager* FileDownloadManager::instance = 0;


FileDownloadManager::FileDownloadManager() {
    qDebug() << "FileDownloadManager::FileDownloadManager";
    initializeDownloaders();
    setConnectionsWithDownloaders();
}

FileDownloadManager* FileDownloadManager::getInstance() {
    if (!instance) {
        instance = new FileDownloadManager();
    }
    return instance;
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

void FileDownloadManager::setConnectionsWithDownloaders() {
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

