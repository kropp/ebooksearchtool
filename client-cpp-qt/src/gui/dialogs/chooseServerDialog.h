#ifndef _choose_Server_Dialog_h_
#define _choose_Server_Dialog_h_

#include <QDialog>
#include <QMap>

class QLabel;
class QRadioButton;

class ChooseServerDialog : public QDialog {

Q_OBJECT

public:
    ChooseServerDialog (QWidget* parent);

private:
    void initialiseMap();

private:
    QMap<const QString, const QString> myServers; // server->opensearch
};
#endif // _choose_Server_Dialog_h_
