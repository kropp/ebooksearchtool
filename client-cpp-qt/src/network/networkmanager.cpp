#include "networkmanager.h"
#include "httpconnection.h"

NetworkManager* NetworkManager::instance = 0;

NetworkManager* NetworkManager::getInstance() {
    if (instance == 0) {
        instance = new NetworkManager();
    }
    return instance;
}

NetworkManager::NetworkManager() {

}

