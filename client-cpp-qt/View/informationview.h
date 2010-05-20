#ifndef INFORMATIONVIEW_H
#define INFORMATIONVIEW_H

#include "standardview.h"
#include "../ViewModel/informationviewmodel.h"

class QTextEdit;

class InformationView : public StandardView
{

    Q_OBJECT

public:

    InformationView(QWidget* parent, InformationViewModel* infoVm);

protected:

    virtual void createComponents();
    virtual void layoutComponents();
    virtual void setWindowParameters();
    virtual void setConnections();

private slots:

    void updateInformation(QString );

private:
    InformationViewModel* myViewModel;
    QTextEdit* myText;
};

#endif // INFORMATIONVIEW_H
