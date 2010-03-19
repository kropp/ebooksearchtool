#ifndef _serversListView_h_
#define _serversListView_h_

#include <QWidget>

class ServersListView : public QWidget {

Q_OBJECT

public:
    ServersListView(QWidget* parent);

private slots:
    void linkActivated(const QString&);


private:
    void setBackground();

private:
    ServersListView(const ServersListView& other);
    ServersListView& operator= (const ServersListView& other);
};

#endif // _serversListView_h_
