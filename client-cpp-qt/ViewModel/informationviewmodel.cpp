#include <QDebug>

#include "informationviewmodel.h"
#include "../Model/book.h"

InformationViewModel::InformationViewModel() {
    myBook = 0;
}

QString InformationViewModel::getInformation() const{
    QString information;
    if (myBook) {

        // set Title
        information.append("<p><b>" + tr(" Title: ") + "</b>");
        information.append(myBook->getTitle() + "</p>");

        // set Authors
        information.append("<p><b>" + tr("Author: ") + "</b>");
        const QVector <const Author*> authors = myBook->getAuthors();
        foreach (const Author* author, authors) {
            information.append(author->getName() + "  ");
        }
        information.append("</p>");
        
        // set Language
        if (!myBook->getLanguage().isEmpty())
            information.append("<p><b>" + tr("\nLanguage: ") + "</b>" + myBook->getLanguage() + "</p>");
        
        // set Summary
        if (!myBook->getSummary().isEmpty())
            information.append("<p> <b>" + tr("\nSummary: ") + "</b>" + myBook->getSummary() + "</p>");

        // set Categories
        if (!myBook->getCategories().isEmpty()) {
            information.append("<p><b>" + tr("Category: ") + "</b>");
            const QVector <QString> categories = myBook->getCategories();
            foreach (const QString category, categories) {
                information.append(category + "  ");
            }
            information.append("</p>");
        }
        // set Issued
        if (!myBook->getIssued().isEmpty())
            information.append("<p><b>" + tr("\nIssued: ") + "</b>" + myBook->getIssued() + "</p>");

        // set Publisher
        if (!myBook->getPublisher().isEmpty())
            information.append("<p><b>" + tr("\nPublisher: ") + "</b>" + myBook->getPublisher() + "</p>");

        // set Updated
//        if (!myBook->getUpdated().isEmpty())
//            information.append("<p><b>" + tr("\nUpdated: ") + "</b>" + myBook->getUpdated() + "</p>");
        // set Rights
        if (!myBook->getRights().isEmpty())
            information.append("<p> <b>" + tr("\nRights: ") + "</b>" + myBook->getRights() + "</p>");
        // set Content
        if (!myBook->getContent().isEmpty()) {
            information.append("<p><b>" + tr("\nContent: ") + "</b>");
            information.append(myBook->getContent() + "</p>");
        }
        // set Downloads information
        if (!myBook->getSourceLinks().isEmpty()) {
            information.append(("<p><b>" + tr("\nAvailable formats: ") + "</b>"));
            QList<QString> formats = myBook->getSourceLinks().keys();
            foreach (const QString format, formats) {
                information.append(format + "\t");
            }

            information.append(("<p><b>" + tr("\nLinks: ") + "</b>"));
            QList<QString> links = myBook->getSourceLinks().values();
            foreach (const QString link, links) {
                information.append(link + "\t");
            }
        } else {
            information.append(("<p><b>" + tr("\nTher is no links for free downloading: ") + "</b>"));
        }

    }
    return information;
}

void InformationViewModel::changeCurrentBook(Book* newBook) {
    myBook = newBook;
    emit bookInformationChanged(getInformation());
}

void InformationViewModel::forgetInfo() {
    myBook = 0;
    qDebug() << "InformationViewModel::forgetInfo()";
    emit bookInformationChanged(QString());
}

