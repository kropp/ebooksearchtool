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
    QString textContent(tr("Title: "));
    textContent.append(book.getTitle());
    textContent.append(tr("\nAuthor: "));
    QVector <const Author*> authors = book.getAuthors();
    foreach (const Author* author, authors) {
        textContent.append(author->getName() + "  ");
    }
    
    if (!book.getLanguage().isEmpty())
        textContent.append(tr("\nLanguage: ") + book.getLanguage());
    
    if (!book.getSummary().isEmpty())
        textContent.append(tr("\nSummary: ") + book.getSummary());
    
    if (!book.getContent().isEmpty())
        textContent.append(tr("\nContent: ") + book.getContent());
        
    text->setText(textContent);
    text->setReadOnly(true);
}
