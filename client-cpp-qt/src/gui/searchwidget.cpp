#include <QLineEdit>
#include <QComboBox>
#include <QPushButton>
#include <QHBoxLayout>

#include "searchwidget.h"
  
SearchWidget::SearchWidget(QWidget* parent) : QWidget(parent) {
	myLineEdit = new QLineEdit(this);
    myComboBox = new QComboBox(this);
    fillComboBox();
	myPushButton = new QPushButton(tr("Search"), this);
//    myLineEdit->setBuddy(myPushButton);
    //for being default search button need to have a focus
    myPushButton->setDefault(true);
    
    QHBoxLayout* mainLayout = new QHBoxLayout(this);
    mainLayout->addWidget(myComboBox);
    mainLayout->addWidget(myLineEdit);
    mainLayout->addWidget(myPushButton);
    setLayout(mainLayout);
}

void SearchWidget::fillComboBox() {
    myComboBox->addItem("everywhere");
    myComboBox->addItem("by title");
    myComboBox->addItem("by author");
}
