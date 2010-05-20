#ifndef INFORMATIONVIEWMODEL_H
#define INFORMATIONVIEWMODEL_H

#include <QObject>

class Book;

class InformationViewModel : public QObject {

    Q_OBJECT

public:
    InformationViewModel();

signals:
    void bookInformationChanged(QString);

public slots:
    void changeCurrentBook(Book*);

public:
    QString getInformation() const;

private:
    Book* myBook;
};

#endif // INFORMATIONVIEWMODEL_H
