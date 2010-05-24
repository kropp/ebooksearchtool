#include <QTextEdit>
#include <QVBoxLayout>

#include <QLabel>
#include <QDebug>
#include <QFile>

#include "informationview.h"

InformationView::InformationView(QWidget* parent, InformationViewModel* infoVm)
    : StandardView(parent), myViewModel(infoVm){
    initialize();
}


void InformationView::createComponents() {
    myText = new QTextEdit(this);
    myText->hide();
    myText->setReadOnly(true);
}

void InformationView::layoutComponents() {
    QVBoxLayout* layout = new QVBoxLayout();
    layout->addWidget(myText);

    setLayout(layout);
}

void InformationView::setWindowParameters() {
    QFile styleSheetFile(":/qss/InformationStyle");
    styleSheetFile.open(QIODevice::ReadOnly);
    this->setStyleSheet(styleSheetFile.readAll());
    styleSheetFile.close();
//    setAutoFillBackground(true);
}

void InformationView::setConnections() {
    connect (myViewModel, SIGNAL(bookInformationChanged(QString)), this, SLOT(updateInformation(QString)));
}

void InformationView::updateInformation(QString info) {
    if (info.isEmpty()) {
        myText->hide();
    } else {
        myText->show();
    }
    qDebug() << "InformationView::updateInformation();";
    myText->clear();
    myText->insertHtml(info);
}
