#include <QLineEdit>
#include <QComboBox>
#include <QPushButton>
#include <QHBoxLayout>

#include "searchwidget.h"
  
SearchWidget::SearchWidget(QWidget* parent) : QWidget(parent) {
	myLineEdit = new QLineEdit(this);
//TODO don't set concrete size!!
    myLineEdit->setMaximumWidth(150);
//    myComboBox = new QComboBox(this);
  //  fillComboBox();
	myPushButton = new QPushButton(QIcon("view/images/search.gif"), tr(""), this);
//    myLineEdit->setBuddy(myPushButton);
    myPushButton->setIconSize(QSize(20, 20));
    myPushButton->setFixedSize(QSize(20, 20));
    myPushButton->setFlat(true);
    //for being default search button need to have a focus
    myPushButton->setDefault(true);
    
    QHBoxLayout* mainLayout = new QHBoxLayout(this);
//    mainLayout->addWidget(myComboBox);
    mainLayout->addWidget(myLineEdit);
    mainLayout->addWidget(myPushButton);
//TODO don't set concrete size!!
    mainLayout->setSpacing(1);
    mainLayout->setMargin(1);
    setMaximumWidth(175);
    setLayout(mainLayout);
}

/*void SearchWidget::fillComboBox() {
    myComboBox->addItem("everywhere");
    myComboBox->addItem("by title");
    myComboBox->addItem("by author");
}
*/
