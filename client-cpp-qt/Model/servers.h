#ifndef SERVERS_H
#define SERVERS_H

#include <QString>
#include <QStringList>

static const QString FEEDBOOKS_ID = "www.feedbooks.com";
static const QString MANYBOOKS_ID = "manybooks.net";
static const QString LITRES_ID = "data.fbreader.org";
static const QString SMASHWORDS_ID = "www.smashwords.com";
static const QString BOOKSERVER_ID = "bookserver.archive.org";
static const QString EBOOKSEARCH_ID = "ebooksearch.webfactional.com";

class EBookSearchTool {
public:
    const EBookSearchTool& getInstance();

    static void initializeServers();

public:
    static QStringList ourServers;

private:
    EBookSearchTool() {}
};


#endif // SERVERS_H
