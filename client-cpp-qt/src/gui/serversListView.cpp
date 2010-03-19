#include <QLabel>
#include <QVBoxLayout>
#include <QDebug>
#include <QProcess>

#include "serversListView.h"

static QString SERVER_URL = "feedbooks.com/catalog.atom";
static QString SERVER_NAME = "feedbooks.com";

ServersListView::ServersListView(QWidget* parent) : QWidget(parent) {
    QVBoxLayout* mainLayout = new QVBoxLayout(this);
    // initialize  servers labels
    
    QLabel* label = new QLabel("<a href=\"" + SERVER_URL + "\">" + SERVER_NAME+ "</a>");
    label->setTextFormat(Qt::RichText);
    
    // add labels to layout
    mainLayout->addWidget(label);
    // connect going to links
    // set layout
    connect(label, SIGNAL(linkActivated(const QString&)),
            this, SLOT(linkActivated(const QString&))); 
   
    setBackground();
    setLayout(mainLayout);
} 
  
void ServersListView::linkActivated(const QString& link) {
    qDebug() << "ServersListView::linkActivated " << link;
    QProcess* process = new QProcess();
    process->start("firefox", QStringList(link));
}

void ServersListView::setBackground() {
    QPalette palette;
    palette.setColor(this->backgroundRole(), Qt::white);  // to move color to argument
    setPalette(palette);
    setAutoFillBackground(true);
}


