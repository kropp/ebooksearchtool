#ifndef SERVERSEDITVIEW_H
#define SERVERSEDITVIEW_H

#include <QWidget>

class ServersEditView : public QWidget
{
    Q_OBJECT

    public:

        ServersEditView(QWidget* parent);
        ~ServersEditView();

protected:

        virtual void createComponents();
        virtual void layoutComponents();
        virtual void setWindowParameters();
        virtual void setConnections();
        virtual void initialize();
};

#endif // SERVERSEDITVIEW_H
