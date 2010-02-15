#ifndef _choose_Server_Dialog_h_
#define _choose_Server_Dialog_h_

#include <QDialog>
#include <map>

class QLabel;
class QVBoxLayout;
class QButtonGroup;

class ChooseServerDialog : public QDialog {

Q_OBJECT

public:
    ChooseServerDialog (QWidget* parent);

private slots:
    void chooseServer();

private:
    void initialiseMap();
    void createRadioButtons();

private:
    std::map<const QString, const QString> myServers; // server -> opensearch schema
    QVBoxLayout* myMainLayout;
    QButtonGroup* myButtonGroup;
};

#endif // _choose_Server_Dialog_h_
