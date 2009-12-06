#include <QWidget>

// field for searching - line edin plus search image
// emit signal after reducting and pressing "Enter"

class QLineEdit;
//class QComboBox;
class QPushButton;

class SearchWidget : public QWidget {

public:
    SearchWidget(QWidget* parent);
    
    void fillComboBox();

private:
    QLineEdit* myLineEdit;
  //  QComboBox* myComboBox;
    QPushButton* myPushButton;
};
