#ifndef CATALOGBROWSEBARVIEWMODEL_H
#define CATALOGBROWSEBARVIEWMODEL_H

#include <QObject>

class CatalogBrowseBarViewModel : public QObject
{

    Q_OBJECT

public:

    CatalogBrowseBarViewModel();

signals:

    void backAvailabilityChanged(bool newValue);
    void forwardAvailabilityChanged(bool newValue);
    void upAvailabilityChanged(bool newValue);

public slots:

    void goBack();
    void goForward();
    void goHome();
    void goUp();

public:

    bool getGoBackAvailability();
    bool getGoForwardAvailability();
    bool getGoUpAvailability();

private:

    void setConnections();


};

#endif // CATALOGBROWSEBARVIEWMODEL_H
