#ifndef OPTIONSVIEWMODEL_H
#define OPTIONSVIEWMODEL_H

#include <QObject>

class ServersEditViewModel;

class OptionsViewModel : public QObject
{

    Q_OBJECT

public:

    OptionsViewModel();

signals:

    void bookCountChanged(QString newValue);

public:

    void requestToChangePageBooksCount(QString newValue);
    void requestToChangeProxy(QString newValue);
    void requestToChangeProxyPort(QString newValue);
    void requestToChangeLibraryPath(QString newValue);
    void requestToChangeDownloadFormat(QString newValue);

    QString getPageBooksCount();
    QString getProxy();
    QString getProxyPort();
    QString getLibraryPath();
    QString getDownloadFormat();

    ServersEditViewModel* getServersEditVm();

public slots:

    void applyAllChanges();

private:

    void emitChanges();

private:

    int booksCountValueToChange;
    QString proxy;
    int proxyPort;
    QString libraryPath;
    QString downloadFormat;

    ServersEditViewModel* serversEditVm;
};

#endif // OPTIONSVIEWMODEL_H
