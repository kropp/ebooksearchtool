#include "settings.h"

Settings* Settings::ourInstance = 0;
QString Settings::FORMAT = "pdf";

Settings::Settings() {
    initializeServers();
}

Settings& Settings::getInstance() {
    if (!ourInstance) {
        ourInstance = new Settings();
    }
    return *ourInstance;
}

const QStringList& Settings::getServers() const {
    return ourServers;
}

void Settings::initializeServers() {
    ourServers.append(FEEDBOOKS_ID);
    ourServers.append(MANYBOOKS_ID);
    ourServers.append(LITRES_ID);
    ourServers.append(SMASHWORDS_ID);
    ourServers.append(EBOOKSEARCH_ID);
}

