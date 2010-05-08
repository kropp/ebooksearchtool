#ifndef AUTHOR_H
#define AUTHOR_H

#include <QString>
#include <QVector>
#include <QMap>

class Author {

public:
        Author(QString name, QString uri) : myName (name), myUri(uri) {}

public:
        const QString& getName() const;
        const QString& getUri() const;

private:
        const QString myName;
        const QString myUri;
};

inline const QString& Author::getName() const {
    return myName;
}

inline const QString& Author::getUri() const {
    return myUri;
}

#endif // AUTHOR_H
