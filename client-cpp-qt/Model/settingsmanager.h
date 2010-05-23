#ifndef SETTINGSMANAGER_H
#define SETTINGSMANAGER_H

#include <QSettings>
#include <QCoreApplication>

class SettingsManager : public QObject
{

    Q_OBJECT

private:

    SettingsManager();

public:

    static SettingsManager* getInstance();

signals:

    void libraryPathChanged();

public:

    void setBooksOnPage(int newValue);
    int getBooksOnPage();

    void setProxy(QString newValue);
    QString getProxy();

    void setProxyPort(int newValue);
    int getProxyPort();

    QString getLibraryPath();
    void setLibraryPath(QString newPath);

    QString getCurrentFormat();
    void setCurrentFormat(QString newFormat);

private:

    static SettingsManager instance;

    QSettings* settings;
};

#endif // SETTINGSMANAGER_H
