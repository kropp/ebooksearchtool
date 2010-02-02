#ifndef _INTERNET_CONNECTION_DIALOG_H_
#define _INTERNET_CONNECTION_DIALOG_H_

#include <QDialog>

#include "../../network/connectionParameters.h"

class QLineEdit;
class QLabel;

class InternetConnectionDialog : public QDialog {

Q_OBJECT

public:
    InternetConnectionDialog(QWidget* parent, ConnectionParameters* parameters);

public slots:
    void setProxyEnabled();
    void setProxyDisabled();

private:
    void closeEvent(QCloseEvent*);

private:
    ConnectionParameters* myParameters;
    QLineEdit* myProxy;
    QLineEdit* myPort;
    QLabel* myProxyLabel;
    QLabel* myPortLabel;
};

#endif //_INTERNET_CONNECTION_DIALOG_H_
