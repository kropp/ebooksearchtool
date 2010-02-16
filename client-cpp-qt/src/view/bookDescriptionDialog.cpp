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
    QString textContent("<p><b>" + tr(" Title: ") + "</b>");
    textContent.append(book.getTitle() + "</p>");
    textContent.append("<p><b>" + tr("Author: ") + "</b>");
    QVector <const Author*> authors = book.getAuthors();
    foreach (const Author* author, authors) {
        textContent.append(author->getName() + "  ");
    }
    textContent.append("</p>");
    if (!book.getLanguage().isEmpty())
        textContent.append("<p><b>" + tr("\nLanguage: ") + "</b>" + book.getLanguage() + "</p>");
    
    if (!book.getSummary().isEmpty())
        textContent.append("<p> <b>" + tr("\nSummary: ") + "</b>" + book.getSummary() + "</p>");
    
    if (!book.getContent().isEmpty()) {
        textContent.append("</p><b>" + tr("\nContent: ") + "</b>");
        textContent.append(book.getContent() + "</p>");
    }

    text->insertHtml(textContent);       
    text->setReadOnly(true);
}
