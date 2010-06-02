#ifndef BOOKRESULTVIEW_H
#define BOOKRESULTVIEW_H

#include <QWidget>
#include <QResizeEvent>
#include <QFontMetrics>
#include <QFrame>

class BookResultViewModel;

#include "standardview.h"
#include "multistatebutton.h"
#include "bookresultsview.h"
#include "progressview.h"

class QLabel;
class QPushButton;
class QMouseEvent;
class QFrame;

class BookResultView : public StandardView
{

    Q_OBJECT

private:
    static QColor ourBackgroundSelectedColor;
    static QColor ourBackgroundColor;

public:
    BookResultView(BookResultsView* parent, BookResultViewModel* resultViewModel, bool addToLibraryButton);
    ~BookResultView();

public:

    virtual void resizeEvent (QResizeEvent* event);
    void select();
    void cancelSelection();

signals:
    void bookSelected(BookResultView* );

protected:

    void createComponents();
    void layoutComponents();
    void setWindowParameters();
    void setConnections();

private slots:

    void downloadButtonPressed();
    void addToLibraryButtonPressed();
    void removeFromLibraryButtonPressed();
    void readButtonPressed();
    void bookInfoPressed();
    void setCover(QIcon* coverIcon);

    void bookDownloadStateChanged(QString newState);

private:
    void mousePressEvent  ( QMouseEvent * e );
    void setBackgroundColor(QColor color);


private:

    BookResultViewModel* myViewModel;

    QLabel* myBookPictureLabel;
    QLabel* myBookCoverLabel;
    QLabel* myBookTitleLabel;
    QLabel* myBookAuthorLabel;
    QLabel* myBookLanguageLabel;
    QLabel* myBookServerLabel;
    MultiStateButton* myDownloadButton;
    QPushButton* myAddToLibraryButton;
    QPushButton* myRemoveFromLibraryButton;
    QPushButton* myReadButton;
    ProgressView* myProgressView;

    BookResultsView* myParent;

    QFontMetrics* myMetrics;

    bool myAddToLibraryButtonEnabled;
};

#endif // BOOKRESULTVIEW_H
