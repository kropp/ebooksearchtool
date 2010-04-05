#include <QCoreApplication>
#include <QString>
#include <QDebug>

#include <QUrl>


#include "loader.h"
// test for method NetworkManager::downloadByUrl()  //for http get - in network manager
// works without proxy
// input: [url] [output file name]


// requests to different servers 
// covers
// other files

static const QString REQUEST = "http://www.smashwords.com/atom/search/books?query=john";

int main(int argc, char* argv[]) {
    if (argc < 3) {
        qDebug() << "Usage: ./network [url] [output file name]";
        return 0;
    }
    
    QUrl url(argv[1]);
    if (!url.isValid()) {
        qDebug() << "Usage: ./network [url], you have entered incorrect url";
        return 0;
    }

    QCoreApplication app(argc, argv);

   // setProxy();
    Loader* loader = new Loader();
    QFile* file = new QFile(argv[2]);
    loader->load(argv[1], file);

	return app.exec();
}


