#ifndef _choose_Server_Dialog_h_
#define _choose_Server_Dialog_h_

#include <QDialog>
#include <map>

class QLabel;
class QRadioButton;

class ChooseServerDialog : public QDialog {

Q_OBJECT

public:
    ChooseServerDialog (QWidget* parent);

private:
    void initialiseMap();
    void createRadioButtons();

private:
    std::map<const QString, const QString> myServers; // server -> opensearch schema
};

#endif // _choose_Server_Dialog_h_
