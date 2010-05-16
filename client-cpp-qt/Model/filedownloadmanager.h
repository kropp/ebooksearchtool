#ifndef FILEDOWNLOADMANAGER_H
#define FILEDOWNLOADMANAGER_H

#include <QObject>
#include <QVector>


class FileDownloadManager : public QObject
{

Q_OBJECT

private:

    FileDownloadManager();

public:

    static FileDownloadManager* getInstance();

signals:

    void downloadFinished(bool);

public slots:

    void downloadBook(const Book& book, const QString& filename, QString format);

private slots:

    void childDownloaderFinished(bool success, QVector<Book*>* gotBooks);

private:

    void initializeDownloaders();
    void recreateDownloaders();
    void abortDownloaders();
    void setConnectionsWithDownloaders();
    void disconnectDownloaders();
    void updateFinishedState();

private:

    static FileDownloadManager* instance;

};

#endif // FILEDOWNLOADMANAGER_H
