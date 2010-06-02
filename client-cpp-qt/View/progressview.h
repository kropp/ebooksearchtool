#ifndef PROGRESSVIEW_H
#define PROGRESSVIEW_H

#include <QProgressBar>

class ProgressViewModel;


class ProgressView : public QProgressBar {

    Q_OBJECT

public:
    ProgressView(QWidget* parent, ProgressViewModel* );

private:
     void setConnections();
     void setParameters();

private:
    ProgressViewModel* myViewModel;
};

#endif // PROGRESSVIEW_H
