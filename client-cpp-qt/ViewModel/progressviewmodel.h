#ifndef PROGRESSVIEWMODEL_H
#define PROGRESSVIEWMODEL_H

#include <QObject>

class BookResultViewModel;


class ProgressViewModel : public QObject {

    Q_OBJECT

public:
    ProgressViewModel();
    ~ProgressViewModel();

signals:
    void showProgress();
    void hideProgress();

protected:
    virtual void setConnections();
};

class BookSearchProgressViewModel : public ProgressViewModel {

    Q_OBJECT

public:

    BookSearchProgressViewModel();
    virtual ~BookSearchProgressViewModel();

private:
    virtual void setConnections();
};


class CatalogProgressViewModel : public ProgressViewModel {

    Q_OBJECT

public:

    CatalogProgressViewModel();
    virtual ~CatalogProgressViewModel();

private:
    virtual void setConnections();
};

class BookDownloadProgressViewModel : public ProgressViewModel {

    Q_OBJECT

public:

    BookDownloadProgressViewModel(BookResultViewModel* );
    virtual ~BookDownloadProgressViewModel();

private:
    virtual void setConnections();

private:
    BookResultViewModel* myBookViewModel;
};


#endif // PROGRESSVIEWMODEL_H
