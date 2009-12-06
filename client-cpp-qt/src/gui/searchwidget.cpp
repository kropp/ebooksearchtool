#include <QLineEdit>
#include <QComboBox>
#include <QPushButton>
#include <QHBoxLayout>

#include "searchwidget.h"
  
SearchWidget::SearchWidget(QWidget* parent) : QWidget(parent) {
	myLineEdit = new QLineEdit(this);
//TODO don't set concrete size!!
    myLineEdit->setMaximumWidth(150);
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
//TODO don't set concrete size!!
    setMaximumWidth(350);
    setLayout(mainLayout);
}

void SearchWidget::fillComboBox() {
    myComboBox->addItem("everywhere");
    myComboBox->addItem("by title");
    myComboBox->addItem("by author");
}
