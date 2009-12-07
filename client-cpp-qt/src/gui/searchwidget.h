#include <QWidget>

// field for searching - line edit plus search image
// emit signal after editing the text and pressing "Enter"

class QLineEdit;
//class QComboBox;
class QPushButton;

class SearchWidget : public QWidget {

    Q_OBJECT

public:
    SearchWidget(QWidget* parent);
    
public:
    bool isSearching();

signals:
    void search(QString query);

private slots:
    void emitSearch();
    //void changeFocus();

private:
    void createLineEdit();
    void createPushButton();
    void setWidgets();

    void fillComboBox();

private:
    QLineEdit* myLineEdit;
  //  QComboBox* myComboBox;
    QPushButton* myPushButton;
};
