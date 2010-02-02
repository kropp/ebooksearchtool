#include <QGridLayout>
#include <QRadioButton>

#include "internetConnectionDialog.h"



InternetConnectionDialog::
InternetConnectionDialog(QWidget* parent, ConnectionParameters* parameters)
: QDialog (parent), myParameters(parameters) {

    QGridLayout* mainLayout = new QGridLayout();
    QRadioButton* withoutProxyButton = new QRadioButton(tr("Without proxy"), this);
    mainLayout->addWidget(withoutProxyButton);
    setLayout(mainLayout);
}
