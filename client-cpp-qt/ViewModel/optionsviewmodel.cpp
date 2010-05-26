#include "optionsviewmodel.h"
#include "serverseditviewmodel.h"
#include "../Model/settingsmanager.h"

OptionsViewModel::OptionsViewModel()
{
    serversEditVm = new ServersEditViewModel();
}

ServersEditViewModel* OptionsViewModel::getServersEditVm()
{
    return serversEditVm;
}

void OptionsViewModel::emitChanges()
{
    emit bookCountChanged(QString::number(booksCountValueToChange));
}

void OptionsViewModel::requestToChangePageBooksCount(QString newValue)
{
    booksCountValueToChange = newValue.toInt();
}

void OptionsViewModel::requestToChangeProxy(QString newValue)
{
    proxy = newValue;
}

void OptionsViewModel::requestToChangeProxyPort(QString newValue)
{
    proxyPort = newValue.toInt();
}

void OptionsViewModel::requestToChangeLibraryPath(QString newValue)
{
    libraryPath = newValue;
}

void OptionsViewModel::requestToChangeDownloadFormat(QString newValue)
{
    downloadFormat = newValue;
}

QString OptionsViewModel::getLibraryPath()
{
    return SettingsManager::getInstance()->getLibraryPath();
}

QString OptionsViewModel::getDownloadFormat()
{
    return SettingsManager::getInstance()->getCurrentFormat();
}

QString OptionsViewModel::getPageBooksCount()
{
    return QString::number(SettingsManager::getInstance()->getBooksOnPage());
}

QString OptionsViewModel::getProxy()
{
    return SettingsManager::getInstance()->getProxy();
}

QString OptionsViewModel::getProxyPort()
{
    int port = SettingsManager::getInstance()->getProxyPort();
    return QString::number(port);
}

void OptionsViewModel::applyAllChanges()
{
    SettingsManager* settings = SettingsManager::getInstance();
    settings->setBooksOnPage(booksCountValueToChange);
    settings->setProxy(proxy);
    settings->setProxyPort(proxyPort);
    settings->setLibraryPath(libraryPath);
    settings->setCurrentFormat(downloadFormat);

    emitChanges();
}

