#ifndef BOOKRESULTVIEW_H
#define BOOKRESULTVIEW_H

#include <QWidget>
#include <QResizeEvent>
#include <QFontMetrics>

class BookResultViewModel;

#include "standardview.h"
#include "multistatebutton.h"

class QLabel;
class QPushButton;

class BookResultView : public StandardView
{

    Q_OBJECT

    public:
        BookResultView(QWidget* parent, BookResultViewModel* resultViewModel, bool addToLibraryButton);
        ~BookResultView();

    public:

        virtual void resizeEvent (QResizeEvent* event);

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
        void informationButtonPressed();
        void bookInfoPressed();

    private:

        BookResultViewModel* myViewModel;

        QLabel* myBookPictureLabel;
        QLabel* myBookTitleLabel;
        QLabel* myBookAuthorLabel;
        QLabel* myBookLanguageLabel;
        QLabel* myBookServerLabel;
        MultiStateButton* myDownloadButton;
        QPushButton* myAddToLibraryButton;
        QPushButton* myRemoveFromLibraryButton;
        QPushButton* myReadButton;
        QPushButton* myInformationButton;

        QFontMetrics* myMetrics;

        bool myAddToLibraryButtonEnabled;
};

#endif // BOOKRESULTVIEW_H
