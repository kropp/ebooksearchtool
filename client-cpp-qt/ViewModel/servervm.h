#ifndef SERVERVM_H
#define SERVERVM_H

#include "../Model/servers.h"
#include <QObject>

class ServerViewModel : public QObject
{

    Q_OBJECT

public:

    ServerViewModel(ServerInfo* info);

public:

    QString getServerAlias();
    QString getServerPath();
    QString getServerSearchPath();
    QString getServerAtomPath();

public slots:

    void requestToDeleteServer();

signals:

    void serverDeleted();

private:

    ServerInfo* m_serverInfo;

};

#endif // SERVERVM_H
