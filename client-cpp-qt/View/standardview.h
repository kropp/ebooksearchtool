#ifndef STANDARDVIEW_H
#define STANDARDVIEW_H

#include <QWidget>

class StandardView : public QWidget
{
    Q_OBJECT

    public:

        StandardView(QWidget* parent);
        ~StandardView();

protected:

        virtual void createComponents();
        virtual void layoutComponents();
        virtual void setWindowParameters();
        virtual void setConnections();
        virtual void initialize();
};

#endif // STANDARDVIEW_H
