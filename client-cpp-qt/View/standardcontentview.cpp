#include "standardcontentview.h"

#include <QFile>

StandardContentView::StandardContentView(QWidget* parent) : StandardView(parent)
{

}

void StandardContentView::createComponents()
{
    upperBarLeftFrame = new QFrame(this);
    upperBarRightFrame = new QFrame(this);
    contentLeftFrame = new QFrame(this);
    contentRightFrame = new QFrame(this);

    upperBarLeftFrame->setObjectName("upperBarLeftFrame");
    upperBarRightFrame->setObjectName("upperBarRightFrame");
    contentLeftFrame->setObjectName("contentLeftFrame");
    contentRightFrame->setObjectName("contentRightFrame");
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

    upperBarLeftFrame->setLayout(leftFrameLayout);
    upperBarRightFrame->setLayout(rightFrameLayout);

    mainBarLayout->setMargin(0);
    mainBarLayout->setSpacing(0);
    mainBarLayout->addWidget(upperBarLeftFrame);
    mainBarLayout->addWidget(upperBarRightFrame);

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

    contentLeftFrame->setLayout(leftContentLayout);
    contentRightFrame->setLayout(rightContentVLayout);

    mainContentLayout->setMargin(0);
    mainContentLayout->setSpacing(0);
    
    mainContentLayout->addWidget(contentLeftFrame);
    mainContentLayout->addWidget(contentRightFrame);


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
