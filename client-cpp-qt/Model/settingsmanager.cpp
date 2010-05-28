#include "settingsmanager.h"

static const QString BOOKS_ON_PAGE_COUNT_ID = "BooksOnPage";
static const int BOOKS_ON_PAGE_DEFAULT_VALUE = 15;

static const QString PROXY_ID = "Proxy";
static const QString PROXY_DEFAULT_VALUE = "";

static const QString PROXY_PORT_ID = "ProxyPort";
static const int PROXY_PORT_DEFAULT_VALUE = 0;

static const QString LIBRARY_PATH_ID = "Library";
static const QString LIBRARY_PATH_DEFAULT_VALUE = "library.xml";

static const QString DOWNLOAD_FORMAT_ID = "Format";
static const QString DOWNLOAD_FORMAT_DEFAULT_VALUE = "pdf";

SettingsManager SettingsManager::instance;

SettingsManager* SettingsManager::getInstance()
{
    return &instance;
}

SettingsManager::SettingsManager()
{
   /* QCoreApplication::setOrganizationName("Test");
    QCoreApplication::setOrganizationDomain("test.com");
    QCoreApplication::setApplicationName("Test");*/

    settings = new QSettings("settings3.txt", QSettings::IniFormat);
}

QString SettingsManager::getLibraryPath()
{
    return LIBRARY_PATH_DEFAULT_VALUE;//settings->value(LIBRARY_PATH_ID, LIBRARY_PATH_DEFAULT_VALUE).toString();
}

void SettingsManager::setLibraryPath(QString newPath)
{
    settings->setValue(LIBRARY_PATH_ID, newPath);
    emit libraryPathChanged();
}

QString SettingsManager::getCurrentFormat()
{
    return settings->value(DOWNLOAD_FORMAT_ID, DOWNLOAD_FORMAT_DEFAULT_VALUE).toString();
}

void SettingsManager::setCurrentFormat(QString newFormat)
{
    if (newFormat.isEmpty()) {
        settings->setValue(DOWNLOAD_FORMAT_ID, DOWNLOAD_FORMAT_DEFAULT_VALUE);
    }
     settings->setValue(DOWNLOAD_FORMAT_ID, newFormat);

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
