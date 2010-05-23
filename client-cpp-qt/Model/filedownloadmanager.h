#ifndef FILEDOWNLOADMANAGER_H
#define FILEDOWNLOADMANAGER_H

#include <QObject>
#include <QVector>
#include <QIODevice>
#include <QHttp>

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
    void coverRequestFinished(int, bool);

public:

    QString getReadDefaultLocation();
    QString getCoverDir();
    int downloadCover(QString urlStr, QIODevice* out);

public slots:

    int downloadBook(const Book& book, const QString& filename, const QString& format);
    int readBook(const Book& book, const QString& filename, const QString& format);

private slots:

    void downloadFinished(bool success, QString filename, int request);
    void coverDownloadRequestFinished(int request, bool success);

private:

    void initializeDownloaders();
    void abortDownloaders();
    void setConnections();

private:

    static FileDownloadManager* instance;

    FileDownloader* myDownloader;

    QHttp* myConnectionForCovers;
};

#endif // FILEDOWNLOADMANAGER_H
