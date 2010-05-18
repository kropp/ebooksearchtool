#ifndef FILEDOWNLOADMANAGER_H
#define FILEDOWNLOADMANAGER_H

#include <QObject>
#include <QVector>

class FileDownloader;
class Book;

class FileDownloadManager : public QObject
{

Q_OBJECT

private:

    FileDownloadManager();

public:

    static FileDownloadManager* getInstance();

signals:

    void downloadBookFinished(bool, int);

public slots:

    int downloadBook(const Book& book, const QString& filename, const QString& format);

private slots:

    void downloadFinished(bool success, QString filename, int request);

private:

    void initializeDownloaders();
    void abortDownloaders();
    void setConnectionsWithDownloaders();

private:

    static FileDownloadManager* instance;

    FileDownloader* myDownloader;
};

#endif // FILEDOWNLOADMANAGER_H
