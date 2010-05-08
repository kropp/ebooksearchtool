#include "search_result.h"

#include <QDebug>

void SearchResult::setLinkToNextResult(const QString& selfLink, const QString& nextLink) {
    // server - link with rel='self' from feed
    // it could be link to that atom page or to catalog
    
    if (nextLink.isEmpty()) {
        return;
    }
    QString server;
    QString fullLink(nextLink);    
    
    // if self link isn't empty 
    // we suppose that next link - isn't relational
    server = selfLink.isEmpty() ? getServerFromUrl(nextLink): 
                                  getServerFromUrl(selfLink);
    
    if (!selfLink.contains('?')) {
        fullLink.prepend("/");
        fullLink.prepend(selfLink); 
    }

//    qDebug() << "SearchResult::insert server: " << server << "  full link: " << fullLink;
    myLinksToNextAtomPage.insert(server, fullLink);
} 
    
    
QString SearchResult::getServerFromUrl(const QString& url) {
    QString server(url);
    server.remove("http://");
    server.remove("https://");
    return server.section('/', 0, 0);
}

void SearchResult::clearResults()
{
    myLinksToNextAtomPage.clear();
}
    
QList<QString>* SearchResult::getLinks() {
    QList<QString>* list = new QList<QString>(myLinksToNextAtomPage.values());
    return list;
}
