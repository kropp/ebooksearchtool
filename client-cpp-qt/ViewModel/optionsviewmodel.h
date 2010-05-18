#ifndef OPTIONSVIEWMODEL_H
#define OPTIONSVIEWMODEL_H

#include <QObject>

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

    QString getPageBooksCount();
    QString getProxy();
    QString getProxyPort();

public slots:

    void applyAllChanges();

private:

    void emitChanges();

private:

    int booksCountValueToChange;
    QString proxy;
    int proxyPort;

};

#endif // OPTIONSVIEWMODEL_H
