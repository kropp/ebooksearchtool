#include <QLineEdit>
#include <QComboBox>
#include <QPushButton>
#include <QHBoxLayout>

#include "searchwidget.h"
  
#include <QDebug>

QString SearchWidget::
ourOpensearchSchema = "http://feedbooks.com/books/search.atom?query=";

void SearchWidget::setOpensearchSchema(const QString& str) {
    ourOpensearchSchema = str;
}

SearchWidget::SearchWidget(QWidget* parent) : QWidget(parent) {
    createLineEdit();
    createPushButton();
    setWidgets();
    setMaximumWidth(175);
//    setFrame(true);
    connect(myPushButton, SIGNAL(clicked()), this, SLOT(emitSearch()));
}

// was in this widget earlier:
// myComboBox = new QComboBox(this);
// fillComboBox();

void SearchWidget::createLineEdit() {
    myLineEdit = new QLineEdit(this);
    myLineEdit->setFrame(false);
    //TODO don't set concrete size!!
    myLineEdit->setMaximumWidth(150);
}

void SearchWidget::createPushButton() {
	myPushButton = new QPushButton(QIcon("view/images/search.gif"), tr(""), this);
//    myLineEdit->setBuddy(myPushButton);
    int size = myLineEdit->size().height();
    myPushButton->setIconSize(QSize(size - 4, size - 4));
    myPushButton->setFixedSize(QSize(size, size));
    myPushButton->setFlat(true);
    //for being default search button need to have a focus
    myPushButton->setDefault(true);
    //myPushButton->setAutoDefault(true);
    myPushButton->setCursor(Qt::PointingHandCursor);
}

void SearchWidget::setWidgets() {
    QHBoxLayout* mainLayout = new QHBoxLayout(this);
//    mainLayout->addWidget(myComboBox);
    mainLayout->addWidget(myLineEdit);
    mainLayout->addWidget(myPushButton);
    mainLayout->setSpacing(0);
 //   mainLayout->setMargin(1);
    setLayout(mainLayout);
}

void SearchWidget::emitSearch() {
    QString str = myLineEdit->text();
    if (!str.isEmpty()) {
        // translate query text to opensearch query
        QString query = myLineEdit->text();
        query.prepend(ourOpensearchSchema);
        qDebug() << "SearchWidget::emit Search " << query;
        emit search(query); 
    } 
}

/*void SearchWidget::fillComboBox() {
    myComboBox->addItem("everywhere");
    myComboBox->addItem("by title");
    myComboBox->addItem("by author");
}
*/
