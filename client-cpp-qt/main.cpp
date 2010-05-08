#include <QApplication>

#include "View/mainview.h"

static const QString SERVER_FEEDBOOKS = "feedbooks.com";

int main(int argc, char *argv[]) {
        QApplication app(argc, argv);
        MainView mainWindow;        
        mainWindow.show();
        return app.exec();
}

