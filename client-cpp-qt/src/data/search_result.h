#ifndef _search_result_h_
#define _search_result_h_

#include <QMap>
#include <QString>

class SearchResult {

public:
    static QString getServerFromUrl(const QString& url);

public:
    SearchResult() {}
    void setLinkToNextResult(const QString& server, const QString& link);   
    // gives all links and stays empty
    void getLinks(QList<QString>&);
    bool hasNextResult();

private:
// server -> continuation of search results
    QMap<QString, QString> myLinksToNextAtomPage; 

private:
    SearchResult(const SearchResult& other);
    SearchResult& operator= (const SearchResult& other);
};

inline bool SearchResult::hasNextResult() {
    return !myLinksToNextAtomPage.empty();
}

#endif
