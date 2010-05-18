#include "settingsmanager.h"

static const QString BOOKS_ON_PAGE_COUNT_ID = "BooksOnPage";
static const int BOOKS_ON_PAGE_DEFAULT_VALUE = 15;

static const QString PROXY_ID = "Proxy";
static const QString PROXY_DEFAULT_VALUE = "";

static const QString PROXY_PORT_ID = "ProxyPort";
static const int PROXY_PORT_DEFAULT_VALUE = 0;

SettingsManager SettingsManager::instance;

SettingsManager::SettingsManager()
{
   /* QCoreApplication::setOrganizationName("Test");
    QCoreApplication::setOrganizationDomain("test.com");
    QCoreApplication::setApplicationName("Test");*/

    settings = new QSettings("settings2.txt");
}

SettingsManager* SettingsManager::getInstance()
{
    return &instance;
}

void SettingsManager::setBooksOnPage(int newValue)
{
    settings->setValue(BOOKS_ON_PAGE_COUNT_ID, newValue);
}

int SettingsManager::getBooksOnPage()
{
    return settings->value(BOOKS_ON_PAGE_COUNT_ID, BOOKS_ON_PAGE_DEFAULT_VALUE).toInt();
}

void SettingsManager::setProxy(QString newValue)
{
    settings->setValue(PROXY_ID, newValue);
}

QString SettingsManager::getProxy()
{
    return settings->value(PROXY_ID, PROXY_DEFAULT_VALUE).toString();
}

void SettingsManager::setProxyPort(int newValue)
{
    settings->setValue(PROXY_PORT_ID, newValue);
}

int SettingsManager::getProxyPort()
{
    return settings->value(PROXY_PORT_ID, PROXY_PORT_DEFAULT_VALUE).toInt();
}
