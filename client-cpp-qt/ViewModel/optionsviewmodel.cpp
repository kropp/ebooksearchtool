#include "optionsviewmodel.h"
#include "../Model/settingsmanager.h"

OptionsViewModel::OptionsViewModel()
{

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
    SettingsManager::getInstance()->setBooksOnPage(booksCountValueToChange);
    SettingsManager::getInstance()->setProxy(proxy);
    SettingsManager::getInstance()->setProxyPort(proxyPort);

    emitChanges();
}

