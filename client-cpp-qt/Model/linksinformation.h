#ifndef LINKSINFORMATION_H
#define LINKSINFORMATION_H

#include <QStringList>

class LinksInformation {

public:
    LinksInformation() {}

public:
     void addNewLink(const QString& link);
     void addPopularLink(const QString& link);

     const QStringList& getNewLinks() const;
     const QStringList& getPolularLinks() const;

private:
    QStringList myNewLinks;
    QStringList myPopularLinks;

private:
    LinksInformation(const LinksInformation& other);
    LinksInformation& operator= (const LinksInformation& other);
};

inline void LinksInformation::addNewLink(const QString& link) {
    myNewLinks.append(link);
}

inline void LinksInformation::addPopularLink(const QString& link) {
    myPopularLinks.append(link);
}

inline const QStringList& LinksInformation::getNewLinks() const {
    return myNewLinks;
}

inline const QStringList& LinksInformation::getPolularLinks() const {
    return myPopularLinks;
}

#endif // LINKSINFORMATION_H
