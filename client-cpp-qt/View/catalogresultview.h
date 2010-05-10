#ifndef CATALOGRESULTVIEW_H
#define CATALOGRESULTVIEW_H

#include <QWidget>
#include <QResizeEvent>
#include <QFontMetrics>

class CatalogResultViewModel;

#include "standardview.h"

class QLabel;
class QPushButton;

class CatalogResultView : public StandardView
{

    Q_OBJECT

    public:
        CatalogResultView(QWidget* parent, CatalogResultViewModel* resultViewModel);
        ~CatalogResultView();

    public:

        virtual void resizeEvent (QResizeEvent* event);

    protected:

        void createComponents();
        void layoutComponents();
        void setWindowParameters();
        void setConnections();

    private slots:

        void openButtonPressed();

    private:

        CatalogResultViewModel* viewModel;

        QLabel* catalogPictureLabel;
        QLabel* catalogTitleLabel;

        QPushButton* openButton;
};

#endif // CATALOGRESULTVIEW_H
