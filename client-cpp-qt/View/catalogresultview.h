#ifndef CATALOGRESULTVIEW_H
#define CATALOGRESULTVIEW_H

#include <QWidget>
#include <QResizeEvent>
#include <QFontMetrics>

class CatalogResultViewModel;
class CatalogResultsView;

#include "standardview.h"

class QLabel;
class QPushButton;

class CatalogResultView : public StandardView
{

    Q_OBJECT

    public:
        CatalogResultView(CatalogResultsView* parent, CatalogResultViewModel* resultViewModel);
        ~CatalogResultView();

    protected:

        void createComponents();
        void layoutComponents();
        void setWindowParameters();
        void setConnections();

    signals:

        void openCatalogRequested();

    private slots:

        void openButtonPressed();

    private:

        CatalogResultViewModel* viewModel;
        CatalogResultsView* myParent;

//        QLabel* catalogPictureLabel;
        QLabel* catalogTitleLabel;
        QLabel* catalogSummaryLabel;


        QPushButton* myOpenCatalogButton;
    };

#endif // CATALOGRESULTVIEW_H
