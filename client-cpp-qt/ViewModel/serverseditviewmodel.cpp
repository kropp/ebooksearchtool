#include "serverseditviewmodel.h"
#include "servervm.h"

#include "../Model/servers.h"

ServersEditViewModel::ServersEditViewModel()
{
    recreateServerVms();
    setConnections();
}

void ServersEditViewModel::recreateServerVms()
{
    serverViewModels.clear();

    foreach (ServerInfo* serverInfo, EBookSearchTool::getInstance()->getServers())
    {
        serverViewModels.append(new ServerViewModel(serverInfo));
    }

    emit serverVmsChanged();
}

void dropServersToDefault()
{
    EBookSearchTool::getInstance()->dropServersToDefault();
}

void ServersEditViewModel::setConnections()
{
    connect (EBookSearchTool::getInstance(), SIGNAL(serversChanged()), this, SLOT(recreateServerVms()));
}

void ServersEditViewModel::requestToAddServer(QString alias, QString serverPath, QString searchPath, QString atomPath, bool bookSearchInclude, bool catalogSearchInclude)
{
    EBookSearchTool::getInstance()->addServer(alias, serverPath, searchPath, atomPath, bookSearchInclude, catalogSearchInclude);
}

QList<ServerViewModel*> ServersEditViewModel::getServerViewModels()
{
    return serverViewModels;
}

void ServersEditViewModel::dropServersToDefault()
{
    EBookSearchTool::getInstance()->dropServersToDefault();
}
