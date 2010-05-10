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

        QLabel* groupLabel;
        QComboBox* groupCombo;

        QLabel* sortLabel;
        QComboBox* sortCombo;

        QLabel* filterGroupLabel;
        QComboBox* filterCombo;

        QLabel* filterWordsLabel;
        QLineEdit* filterEditBox;
        QPushButton* performButton;

        SelectionType selectedGroupType;
        SelectionType selectedSortType;
        SelectionType selectedFilterType;
        QString selectedFilterTerm;

        QFrame* filterFrame;

        BookResultsViewModel* viewModel;

};

#endif // SELECTIONFILTERVIEW_H
