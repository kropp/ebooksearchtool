#ifndef SERVERSEDITVIEW_H
#define SERVERSEDITVIEW_H

#include <QWidget>
#include <QScrollArea>

class ServersEditViewModel;

#include "standardview.h"
#include "multistatebutton.h"

#include "serverview.h"

class ServersEditView : public StandardView
{
    Q_OBJECT

public:

    ServersEditView(QWidget* parent, ServersEditViewModel* viewModel);
    ~ServersEditView();

protected:

    virtual void createComponents();
    virtual void layoutComponents();
    virtual void setWindowParameters();
    virtual void setConnections();
    virtual void initialize();

private slots:

    void recreateServerViews();
    void serversChanged();
    void addServerPressed();
    void defaultServersPressed();

private:

    ServersEditViewModel* myViewModel;

    QList<ServerView*> serverViews;
    QLabel* serversLabel;

    QLabel* newServerLabel;
    QLabel* aliasLabel;
    QLabel* serverPathLabel;
    QLabel* serverSearchPathLabel;
    QLabel* serverAtomPathLabel;

    QPushButton* addServerButton;
    QLineEdit* aliasEdit;
    QLineEdit* serverPathEdit;
    QLineEdit* searchPathEdit;
    QLineEdit* atomPathEdit;

    QPushButton* dropServersButton;
};

#endif // SERVERSEDITVIEW_H
