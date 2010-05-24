#ifndef OPTIONSLISTVIEW_H
#define OPTIONSLISTVIEW_H

#include <QVector>
#include <QVBoxLayout>
#include <QHBoxLayout>
#include <QLabel>
#include <QLineEdit>
#include <QPushButton>
#include <QScrollArea>

#include "standardview.h"

class OptionsViewModel;
class ServersEditView;

class OptionsListView : public StandardView
{

    Q_OBJECT

    public:
        OptionsListView(QWidget* parent, OptionsViewModel* resultsViewModel);
        ~OptionsListView();

    protected:

        void createComponents();
        void layoutComponents();
        void setWindowParameters();
        void setConnections();

        void resizeEvent(QResizeEvent* event);

    protected slots:

        void booksCountChanged(QString newValue);
        void applyAllChanges();

    private:

        OptionsViewModel* myViewModel;

        QLabel* booksPerPageLabel;
        QLineEdit* booksPerPageEdit;

        QLabel* proxyLabel;
        QLineEdit* proxyEdit;

        QLabel* proxyPortLabel;
        QLineEdit* proxyPortEdit;

//        QLabel* libraryPathLabel;
//        QLineEdit* libraryPathEdit;

        QLabel* downloadFormatLabel;
        QLineEdit* downloadFormatEdit;

        ServersEditView* serversList;

        QPushButton* applyButton;

        QScrollArea* scrollArea;

};
#endif // OPTIONSLISTVIEW_H
