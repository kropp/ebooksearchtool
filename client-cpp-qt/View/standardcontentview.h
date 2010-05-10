#ifndef STANDARDCONTENTVIEW_H
#define STANDARDCONTENTVIEW_H

#include <QObject>
#include <QFrame>
#include <QHBoxLayout>
#include "standardview.h"

class StandardContentView : public StandardView
{

    Q_OBJECT

public:

    StandardContentView(QWidget* parent);

protected:

    virtual void createComponents();
    virtual void layoutComponents();
    virtual void setWindowParameters();
    virtual void setConnections();

    virtual void addItemsToLeftBarPartLayout(QHBoxLayout* leftPartLayout);
    virtual void addItemsToRightBarPartLayout(QHBoxLayout* rightPartLayout);

    virtual void addItemsToLeftContentPartLayout(QHBoxLayout* leftPartLayout);
    virtual void addItemsToRightContentPartLayout(QHBoxLayout* rightPartLayout);

private:

    QFrame* upperBarLeftFrame;
    QFrame* upperBarRightFrame;
    QFrame* contentLeftFrame;
    QFrame* contentRightFrame;
};

#endif // STANDARDCONTENTVIEW_H
