#include <QLineEdit>
#include <QComboBox>
#include <QPushButton>
#include <QHBoxLayout>

#include "searchwidget.h"
  
#include <QDebug>

SearchWidget::SearchWidget(QWidget* parent) : QWidget(parent) {
    createLineEdit();
    createPushButton();
    setWidgets();
    setMaximumWidth(175);

    connect(myPushButton, SIGNAL(clicked()), this, SLOT(emitSearch()));
}

// was in this widget earlier:
// myComboBox = new QComboBox(this);
// fillComboBox();

void SearchWidget::createLineEdit() {
    myLineEdit = new QLineEdit(this);
    //TODO don't set concrete size!!
    myLineEdit->setMaximumWidth(150);
}

void SearchWidget::createPushButton() {
	myPushButton = new QPushButton(QIcon("view/images/search.gif"), tr(""), this);
//    myLineEdit->setBuddy(myPushButton);
    myPushButton->setIconSize(QSize(20, 20));
    myPushButton->setFixedSize(QSize(20, 20));
    myPushButton->setFlat(true);
    //for being default search button need to have a focus
    myPushButton->setDefault(true);
}

void SearchWidget::setWidgets() {
    QHBoxLayout* mainLayout = new QHBoxLayout(this);
//    mainLayout->addWidget(myComboBox);
    mainLayout->addWidget(myLineEdit);
    mainLayout->addWidget(myPushButton);
//    mainLayout->setSpacing(1);
 //   mainLayout->setMargin(1);
    setLayout(mainLayout);
}

void SearchWidget::emitSearch() {
    QString str = myLineEdit->text();
    if (!str.isEmpty()) {
        emit search(myLineEdit->text());
    } 
}

/*void SearchWidget::fillComboBox() {
    myComboBox->addItem("everywhere");
    myComboBox->addItem("by title");
    myComboBox->addItem("by author");
}
*/
