#include <QFile>

#include "progressview.h"
#include "../ViewModel/progressviewmodel.h"


ProgressView::ProgressView(QWidget* parent, ProgressViewModel* viewModel)
    : QProgressBar(parent), myViewModel(viewModel)
{
    hide();
    setConnections();
    setParameters();

}

void ProgressView::setConnections() {
    connect(myViewModel, SIGNAL(showProgress()), this, SLOT(show()));
    connect(myViewModel, SIGNAL(hideProgress()), this, SLOT(hide()));
}

void ProgressView::setParameters() {
    setMaximum(0);
    setMinimum(0);
    setObjectName("progressBar");
    QFile styleSheetFile(":/qss/BookStyle");
    styleSheetFile.open(QIODevice::ReadOnly);
    this->setStyleSheet(styleSheetFile.readAll());
    styleSheetFile.close();
}

