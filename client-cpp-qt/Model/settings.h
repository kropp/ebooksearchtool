#ifndef SETTINGS_H
#define SETTINGS_H

#include <QString>
#include <QStringList>

static const QString FEEDBOOKS_ID = "www.feedbooks.com";
static const QString MANYBOOKS_ID = "manybooks.net";
static const QString LITRES_ID = "data.fbreader.org";
static const QString SMASHWORDS_ID = "www.smashwords.com";
static const QString BOOKSERVER_ID = "bookserver.archive.org";
static const QString EBOOKSEARCH_ID = "ebooksearch.webfactional.com";


class Settings {

public:
    static QString FORMAT;

    static Settings& getInstance();

private:
    static Settings* ourInstance;


public:
    const QStringList& getServers() const;
    const QString& getLibraryPath() const;

private:
    Settings();
    void initializeServers();

private:

   QStringList myServers;
   QString myLibraryPath;

};

inline const QString& Settings::getLibraryPath() const {
    return myLibraryPath;
}


#endif // SETTINGS_H
