#ifndef SETTINGSMANAGER_H
#define SETTINGSMANAGER_H

#include <QSettings>
#include <QCoreApplication>

class SettingsManager
{
private:

    SettingsManager();

public:

    static SettingsManager* getInstance();

public:

    void setBooksOnPage(int newValue);
    int getBooksOnPage();

    void setProxy(QString newValue);
    QString getProxy();

    void setProxyPort(int newValue);
    int getProxyPort();

private:

    static SettingsManager instance;

    QSettings* settings;
};

#endif // SETTINGSMANAGER_H
