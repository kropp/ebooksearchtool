#ifndef SELECTIONFILTERVIEW_H
#define SELECTIONFILTERVIEW_H

#include <QObject>
#include <QLabel>
#include <QComboBox>
#include <QLineEdit>
#include <QPushButton>

#include "../ViewModel/bookresultsviewmodel.h"
#include "standardview.h"

class SelectionFilterView : public StandardView
{

    Q_OBJECT

public:

    SelectionFilterView(QWidget* parent, BookResultsViewModel* bookResultViewModel);

protected:

        virtual void createComponents();
        virtual void layoutComponents();
        virtual void setWindowParameters();
        virtual void setConnections();

private slots:

        void groupComboActivated(const QString& string);
        void sortComboActivated(const QString& string);
        void filterComboActivated(const QString& string);
        void filterTextChanged(QString newText);
        void performFilterPressed();

private:

        QLabel* myGroupLabel;
        QComboBox* myGroupCombo;

        QLabel* mySortLabel;
        QComboBox* mySortCombo;

        QLabel* myFilterGroupLabel;
        QComboBox* myFilterCombo;

        QLabel* myFilterWordsLabel;
        QLineEdit* myFilterEditBox;
        QPushButton* myPerformButton;

        SelectionType mySelectedGroupType;
        SelectionType mySelectedSortType;
        SelectionType mySelectedFilterType;
        QString mySelectedFilterTerm;

        QFrame* myFilterFrame;

        BookResultsViewModel* myViewModel;

};

#endif // SELECTIONFILTERVIEW_H
