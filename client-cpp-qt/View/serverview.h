#ifndef SERVERVIEW_H
#define SERVERVIEW_H

#include <QWidget>
#include <QResizeEvent>
#include <QFontMetrics>
#include <QFrame>
#include <QLineEdit>
#include <QLabel>
#include <QPushButton>
#include <QMouseEvent>
#include <QFrame>
#include <QObject>

class ServerViewModel;

#include "standardview.h"
#include "multistatebutton.h"

class ServerView : public StandardView
{

    Q_OBJECT


public:

    ServerView(QWidget* parent, ServerViewModel* resultViewModel);
    ~ServerView();

protected:

    void createComponents();
    void layoutComponents();
    void setWindowParameters();
    void setConnections();

private slots:

    void deleteButtonPressed();

signals:

    void serverDeleted();

private:

    ServerViewModel* myViewModel;

    QLabel* serverAliasLabel;
    QLabel* serverPathLabel;
    QLabel* serverSearchPathLabel;
    QLabel* serverAtomPathLabel;
    QPushButton* deleteButton;
    QLineEdit* serverPathEdit;
    QLineEdit* searchPathEdit;
    QLineEdit* atomPathEdit;
};

#endif // SERVERVIEW_H
