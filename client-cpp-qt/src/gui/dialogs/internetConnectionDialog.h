#ifndef _INTERNET_CONNECTION_DIALOG_H_
#define _INTERNET_CONNECTION_DIALOG_H_

#include <QDialog>

#include "../../network/connectionParameters.h"

class InternetConnectionDialog : public QDialog {

public:
    InternetConnectionDialog(QWidget* parent, ConnectionParameters* parameters);

private:
    ConnectionParameters* myParameters;
};

#endif //_INTERNET_CONNECTION_DIALOG_H_
