#include "standardcontentview.h"

#include <QFile>

StandardContentView::StandardContentView(QWidget* parent) : StandardView(parent)
{

}

void StandardContentView::createComponents()
{
    myUpperBarLeftFrame = new QFrame(this);
    myUpperBarRightFrame = new QFrame(this);
    myContentLeftFrame = new QFrame(this);
    myContentRightFrame = new QFrame(this);

    myUpperBarLeftFrame->setObjectName("upperBarLeftFrame");
    myUpperBarRightFrame->setObjectName("upperBarRightFrame");
    myContentLeftFrame->setObjectName("contentLeftFrame");
    myContentRightFrame->setObjectName("contentRightFrame");
}

void StandardContentView::layoutComponents()
{
    QVBoxLayout* mainLayout = new QVBoxLayout();
    QHBoxLayout* mainBarLayout = new QHBoxLayout();

    QHBoxLayout* leftFrameLayout = new QHBoxLayout();
    QHBoxLayout* rightFrameLayout = new QHBoxLayout();

    leftFrameLayout->setMargin(0);
    leftFrameLayout->setSpacing(0);
    rightFrameLayout->setMargin(0);
    rightFrameLayout->setSpacing(0);

    leftFrameLayout->addSpacing(40);
    rightFrameLayout->addSpacing(40);
    addItemsToLeftBarPartLayout(leftFrameLayout);
    addItemsToRightBarPartLayout(rightFrameLayout);

    rightFrameLayout->addSpacing(10);

    myUpperBarLeftFrame->setLayout(leftFrameLayout);
    myUpperBarRightFrame->setLayout(rightFrameLayout);

    mainBarLayout->setMargin(0);
    mainBarLayout->setSpacing(0);
    mainBarLayout->addWidget(myUpperBarLeftFrame);
    mainBarLayout->addWidget(myUpperBarRightFrame);

    QHBoxLayout* mainContentLayout = new QHBoxLayout();

    QHBoxLayout* leftContentLayout = new QHBoxLayout();
    QHBoxLayout* rightContentLayout = new QHBoxLayout();

    leftContentLayout->setMargin(0);
    leftContentLayout->setSpacing(0);
    rightContentLayout->setMargin(0);
    rightContentLayout->setSpacing(0);

    leftContentLayout->addSpacing(40);
    addItemsToLeftContentPartLayout(leftContentLayout);
    addItemsToRightContentPartLayout(rightContentLayout);

    rightContentLayout->addSpacing(10);

    QVBoxLayout* rightContentVLayout = new QVBoxLayout();
    rightContentVLayout->addSpacing(20);
    rightContentVLayout->addItem(rightContentLayout);

    myContentLeftFrame->setLayout(leftContentLayout);
    myContentRightFrame->setLayout(rightContentVLayout);

    mainContentLayout->setMargin(0);
    mainContentLayout->setSpacing(0);
    
    mainContentLayout->addWidget(myContentLeftFrame);
    mainContentLayout->addWidget(myContentRightFrame);


    mainLayout->setMargin(0);
    mainLayout->setSpacing(0);
    mainLayout->addItem(mainBarLayout);
    mainLayout->addItem(mainContentLayout);

    this->setLayout(mainLayout);
}

void StandardContentView::setWindowParameters()
{
    QFile styleSheetFile(":/qss/StandardView");
    styleSheetFile.open(QIODevice::ReadOnly);
    this->setStyleSheet(styleSheetFile.readAll());
}

void StandardContentView::setConnections()
{

}

void StandardContentView::addItemsToLeftBarPartLayout(QHBoxLayout* leftPartLayout)
{

}

void StandardContentView::addItemsToRightBarPartLayout(QHBoxLayout* rightPartLayout)
{

}

void StandardContentView::addItemsToLeftContentPartLayout(QHBoxLayout* leftPartLayout)
{

}

void StandardContentView::addItemsToRightContentPartLayout(QHBoxLayout* rightPartLayout)
{

}
