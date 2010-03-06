#include <QVBoxLayout>
#include <QTextEdit>

#include <QDebug>

#include "bookDescriptionDialog.h"

BookDescriptionDialog::
BookDescriptionDialog(QWidget* parent, const Book& book) 
: QDialog(parent) {
    setAttribute(Qt::WA_DeleteOnClose);
   // create widget, setLayout 
    QVBoxLayout* layout = new QVBoxLayout();    
    QTextEdit* bookDescription = new QTextEdit(this);
    setText(bookDescription, book);
    layout->addWidget(bookDescription);
    setLayout(layout);
    
    setWindowTitle(book.getTitle());
}
 
void BookDescriptionDialog::setText(QTextEdit* text, const Book& book) {
    
    // set Title
    QString textContent("<p><b>" + tr(" Title: ") + "</b>");
    textContent.append(book.getTitle() + "</p>");
    
    // set Authors
    textContent.append("<p><b>" + tr("Author: ") + "</b>");
    const QVector <const Author*> authors = book.getAuthors();
    foreach (const Author* author, authors) {
        textContent.append(author->getName() + "  ");
    }
    textContent.append("</p>");

    // set Language
    if (!book.getLanguage().isEmpty())
        textContent.append("<p><b>" + tr("\nLanguage: ") + "</b>" + book.getLanguage() + "</p>");
    
    // set Summary
    if (!book.getSummary().isEmpty())
        textContent.append("<p> <b>" + tr("\nSummary: ") + "</b>" + book.getSummary() + "</p>");
    
    // set Categories
    if (!book.getCategories().isEmpty()) {
        textContent.append("<p><b>" + tr("Category: ") + "</b>");
        const QVector <QString> categories = book.getCategories();
        foreach (const QString category, categories) {
            textContent.append(category + "  ");
        }
        textContent.append("</p>");
    }
    // set Issued
    if (!book.getIssued().isEmpty())
        textContent.append("<p><b>" + tr("\nIssued: ") + "</b>" + book.getIssued() + "</p>");

    // set Publisher
    if (!book.getPublisher().isEmpty())
        textContent.append("<p><b>" + tr("\nPublisher: ") + "</b>" + book.getPublisher() + "</p>");

    // set Updated
    if (!book.getUpdated().isEmpty())
        textContent.append("<p><b>" + tr("\nUpdated: ") + "</b>" + book.getUpdated() + "</p>");
    // set Rights
    if (!book.getRights().isEmpty())
        textContent.append("<p> <b>" + tr("\nRights: ") + "</b>" + book.getRights() + "</p>");
    // set Content
    if (!book.getContent().isEmpty()) {
        textContent.append("</p><b>" + tr("\nContent: ") + "</b>");
        textContent.append(book.getContent() + "</p>");
    }

    text->insertHtml(textContent);       
    text->setReadOnly(true);
}
