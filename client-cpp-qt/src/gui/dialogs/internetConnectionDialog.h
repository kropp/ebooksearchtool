#ifndef _INTERNET_CONNECTION_DIALOG_H_
#define _INTERNET_CONNECTION_DIALOG_H_

#include <QDialog>

#include "../../network/connectionParameters.h"

class QLineEdit;
class QLabel;
class QRadioButton;

class InternetConnectionDialog : public QDialog {

Q_OBJECT

public:
    InternetConnectionDialog(QWidget* parent);

private slots:
    void setProxyEnabled();
    void setProxyDisabled();
    void applyNewSettings();
    void cancel();

private:
    void setInitialValues();
    void closeEvent(QCloseEvent*);
    
private:
    QRadioButton* myWithoutProxyButton;
    QRadioButton* mySetProxyButton;
    QLineEdit* myProxy;
    QLineEdit* myPort;
    QLabel* myProxyLabel;
    QLabel* myPortLabel;
};

#endif //_INTERNET_CONNECTION_DIALOG_H_
