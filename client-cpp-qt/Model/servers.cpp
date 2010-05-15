#include "servers.h"


QStringList EBookSearchTool::ourServers;

void EBookSearchTool::initializeServers() {
    ourServers.append(FEEDBOOKS_ID);
    ourServers.append(MANYBOOKS_ID);
    ourServers.append(LITRES_ID);
    ourServers.append(SMASHWORDS_ID);
    ourServers.append(EBOOKSEARCH_ID);
}
