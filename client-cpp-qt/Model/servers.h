#ifndef SERVERS_H
#define SERVERS_H

#include <QString>
#include <QStringList>
#include <QObject>
#include <QMap>

struct ServerInfo
{
    QString ProgramAlias;
    QString ServerPath;
    QString SearchPath;
    QString RootAtomPath;
    bool includedInBookSearch;
    bool includedInCatalogSearch;
};

class EBookSearchTool : public QObject {

    Q_OBJECT

private:

    EBookSearchTool();

public:

    static EBookSearchTool* getInstance();

    void initializeServers();

signals:

    void serversChanged();

public:

//Server id -> server info
    const QMap<QString, ServerInfo*>& getServers();

public:

    void addServer(QString alias, QString serverPath, QString searchPath, QString atomPath, bool bookSearchInclude, bool catalogSearchInclude);
    void deleteServer(QString serverHost);
    void dropServersToDefault();

private:

    void addServerWithNotification(QString alias, QString serverPath, QString searchPath, QString atomPath, bool bookSearchInclude, bool catalogSearchInclude, bool emitSignal);

private:

    QMap<QString, ServerInfo*> ourServers;

    static EBookSearchTool instance;
};


#endif // SERVERS_H
