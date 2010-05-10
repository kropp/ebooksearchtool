#ifndef CATALOGRESULTSVIEW_H
#define CATALOGRESULTSVIEW_H

#include <QVector>
#include <QVBoxLayout>
#include <QHBoxLayout>

class CatalogResultsViewModel;
class CatalogResultViewModel;
class CatalogResultView;

#include "standardview.h"

class CatalogResultsView : public StandardView
{

    Q_OBJECT

    public:
        CatalogResultsView(QWidget* parent, CatalogResultsViewModel* resultsViewModel);
        ~CatalogResultsView();

    protected:

        void createComponents();
        void layoutComponents();
        void setWindowParameters();
        void setConnections();

    private slots:

        void createChildViews(QVector<CatalogResultViewModel*>* childModels);

    private:

        void relayout();
        void createChildViews();

    private:

        CatalogResultsViewModel* viewModel;

        QVector<CatalogResultView*> shownResults;
        QVector<CatalogResultViewModel*> currentResultVms;

        QVBoxLayout* currentVerticalLayout;
};

#endif // CATALOGRESULTSVIEW_H
