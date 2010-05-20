#include <QTextEdit>
#include <QVBoxLayout>

#include <QLabel>
#include <QDebug>

#include "informationview.h"

InformationView::InformationView(QWidget* parent, InformationViewModel* infoVm)
    : StandardView(parent), myViewModel(infoVm){
    initialize();
}


void InformationView::createComponents() {
    myText = new QTextEdit(this);
    myText->insertHtml("selected book information");
    myText->setReadOnly(true);
}

void InformationView::layoutComponents() {
    QVBoxLayout* layout = new QVBoxLayout();
    layout->addWidget(myText);

    setLayout(layout);
}

void InformationView::setWindowParameters() {

}

void InformationView::setConnections() {
    connect (myViewModel, SIGNAL(bookInformationChanged(QString)), this, SLOT(updateInformation(QString)));
}

void InformationView::updateInformation(QString info) {
    qDebug() << "InformationView::updateInformation();";
    myText->clear();
    myText->insertHtml(info);
}
