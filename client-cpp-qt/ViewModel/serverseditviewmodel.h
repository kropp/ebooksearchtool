#ifndef SERVERSEDITVIEWMODEL_H
#define SERVERSEDITVIEWMODEL_H

#include <QObject>

class ServerViewModel;

class ServersEditViewModel : public QObject
{

    Q_OBJECT

public:

    ServersEditViewModel();

public:

    QList<ServerViewModel*> getServerViewModels();
    void requestToAddServer(QString alias, QString serverPath, QString searchPath, QString atomPath, bool bookSearchInclude, bool catalogSearchInclude);
    void dropServersToDefault();

signals:

    void serverVmsChanged();

private slots:

    void recreateServerVms();

private:

    void setConnections();

private:

    QList<ServerViewModel*> serverViewModels;


};

#endif // SERVERSEDITVIEWMODEL_H
