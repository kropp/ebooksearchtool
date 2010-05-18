#include "settings.h"

Settings* Settings::ourInstance = 0;
QString Settings::FORMAT = "pdf";

Settings::Settings() {
    initializeServers();
    myLibraryPath = "library.xml";
}

Settings& Settings::getInstance() {
    if (!ourInstance) {
        ourInstance = new Settings();
    }
    return *ourInstance;
}

const QStringList& Settings::getServers() const {
    return myServers;
}

void Settings::initializeServers() {
    myServers.append(FEEDBOOKS_ID);
    myServers.append(MANYBOOKS_ID);
    myServers.append(LITRES_ID);
    myServers.append(SMASHWORDS_ID);
    myServers.append(EBOOKSEARCH_ID);
}

