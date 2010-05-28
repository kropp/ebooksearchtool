#include "servervm.h"

ServerViewModel::ServerViewModel(ServerInfo* info)
{
    m_serverInfo = info;
}

QString ServerViewModel::getServerAlias()
{
    return m_serverInfo->ProgramAlias;
}

QString ServerViewModel::getServerPath()
{
    return m_serverInfo->ServerPath;
}

QString ServerViewModel::getServerSearchPath()
{
    return m_serverInfo->SearchPath;
}

QString ServerViewModel::getServerAtomPath()
{
    return m_serverInfo->RootAtomPath;
}

void ServerViewModel::requestToDeleteServer()
{
    EBookSearchTool::getInstance()->deleteServer(m_serverInfo->ServerPath);
    emit serverDeleted();
}
