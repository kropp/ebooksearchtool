#include <QGridLayout>
#include <QRadioButton>
#include <QButtonGroup>

#include "internetConnectionDialog.h"



InternetConnectionDialog::
InternetConnectionDialog(QWidget* parent, ConnectionParameters* parameters)
: QDialog (parent), myParameters(parameters) {

    
    // creating radio buttons
    QRadioButton* withoutProxyButton = new QRadioButton(tr("Without proxy"), this);
    QRadioButton* setProxyButton = new QRadioButton(tr("Set proxy"), this);
    QButtonGroup* buttonGroup = new QButtonGroup(this);
    buttonGroup->addButton(withoutProxyButton);
    buttonGroup->addButton(setProxyButton);

   // adding widgets into layout
    QGridLayout* mainLayout = new QGridLayout();
    mainLayout->addWidget(withoutProxyButton);
    mainLayout->addWidget(setProxyButton);
    
    setLayout(mainLayout);
    setWindowTitle(tr("Internet connection settings"));
}

void InternetConnectionDialog::closeEvent(QCloseEvent*) {
    hide();
}
